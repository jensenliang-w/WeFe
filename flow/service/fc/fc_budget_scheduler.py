# Copyright 2021 Tianmian Tech. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import traceback
from common.python.common.consts import TaskStatus
from flow.utils.budget_utils import BudgetUtils
from common.python.db.global_config_dao import GlobalConfigDao
import threading
from common.python.db.db_models import *
import time
from common.python.utils.log_utils import schedule_logger, LoggerFactory
import psutil
import errno
import os


class FcBudgetScheduler(threading.Thread):
    """
        Detects whether the budget of function computing exceeds the limit,
        and then stops the corresponding task.
    """
    logger = LoggerFactory.get_logger("FcBudgetScheduler")

    def __init__(self):
        super().__init__()
        function_compute_config = GlobalConfigDao.get_function_compute_config()
        self.month_budget = function_compute_config.max_cost_in_month
        self.day_budget = function_compute_config.max_cost_in_day

    def get_running_task(self):
        with DB.connection_context():
            task_list = Task.select().where(
                Task.status == TaskStatus.RUNNING
            ).execute()
            return task_list

    def get_month_budget(self):
        return self.month_budget

    def get_day_budget(self):
        return self.day_budget

    def stop_tasks(self, task_list, is_month=True):
        if is_month:
            self.logger.warn("函数计算已超最大月限额, 随即停止所有任务！")
        else:
            self.logger.warn("函数计算已超最大日限额, 随即停止所有任务！")
        with DB.connection_context():
            for task in task_list:
                killed = self.kill_task(task)
                if killed:
                    self.logger.info(f"kill task {task.name}({task.task_id}) process pid: {task.pid} success!")
                    task.status = TaskStatus.STOP
                    task.message = '函数计算额度已超出，暂停任务'
                    task.save()
                else:
                    self.logger.error(f"failed to kill task {task.name}({task.task_id}) process pid: {task.pid}")

    def kill_task(self, task: Task):
        """"
        Close the process corresponding to the task
        """
        try:
            pid = task.pid
            if not pid:
                return False

            self.logger.debug("try to kill task {}({}) process pid:{}".format(task.name, task.task_id, pid))

            if not self.kill_process(pid):
                return True

            p = psutil.Process(pid)
            for child in p.children(recursive=True):
                if self.kill_process(child.pid):
                    child.kill()

            if self.kill_process(p.pid):
                p.kill()

            return True
        except Exception as e:
            raise e

    def kill_process(self, pid):
        """
        kill a process

        Parameters
        ----------
        pid: int
            The id of process

        """
        if pid < 0:
            return False
        if pid == 0:
            raise ValueError('invalid PID 0')
        try:
            os.kill(pid, 0)
        except OSError as err:
            if err.errno == errno.ESRCH:
                # ESRCH == No such process
                return False
            elif err.errno == errno.EPERM:
                # EPERM clearly means there's a process to deny access to
                return True
            else:
                # According to "man 2 kill" possible error values are
                # (EINVAL, EPERM, ESRCH)
                raise
        else:
            return True

    def run(self):
        budget_util = BudgetUtils()

        while True:
            try:
                month_cost = budget_util.get_month_cost()
                day_cost = budget_util.get_day_cost()

                # Overspend daily or monthly
                if int(self.month_budget) <= month_cost:
                    task_list = self.get_running_task()
                    self.stop_tasks(task_list)
                    break
                elif float(self.day_budget) <= day_cost:
                    task_list = self.get_running_task()
                    self.stop_tasks(task_list, is_month=False)
                    break
                else:
                    # judge once every 10min
                    time.sleep(10 * 60)
            except Exception as e:
                traceback.print_exc()
                schedule_logger().exception("函数计算预算检测出现异常：%s", e)
                time.sleep(5)
                continue

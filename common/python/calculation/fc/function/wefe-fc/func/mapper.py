# Copyright 2021 The WeFe Authors. All Rights Reserved.
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

import json
from comm import dataUtil
from comm.dataUtil import TimeConsume
from common.python.utils import cloudpickle


def handler(event, context):
    """

    map method

    Parameters
    ----------
    event: the data passed in when the user calls the function
    context: contains some information about the runtime of the function like:
            - requestId
            - function
            - credentials
            - service
            - region
            - accountId

    Returns
    -------
    function call result:
        {
            'code': code,
            'message': message,
            'req_id': req_id,
            'count': count,
            'partition': partition
        }

    """
    evt = json.loads(event)
    tc = TimeConsume()

    # get the source and destination fcStorage
    source_fcs, dest_fcs = dataUtil.get_fc_storages(evt)
    # get data
    partition = evt['partition']
    source_k_v = source_fcs.collect(partition=partition, debug_info=dataUtil.get_request_id(context))
    tc.end('get data', evt, context)

    # do map
    func = None
    func_init = False
    map_result = []
    count = 0
    for k, v in source_k_v:
        if not func_init:
            tc.start()
            func = cloudpickle.loads(bytes.fromhex(evt['func']))
            func_init = True
            tc.end('cloudpickle.loads', evt, context)
        count += 1
        map_result.append(func(k, v))

    tc.end('mapper:collect_and_map', evt, context)
    # put result to ots
    if len(map_result) > 0:
        dest_fcs.put_all(map_result)
    return dataUtil.fc_result(count=count, partition=partition)

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

# Copyright 2019 The FATE Authors. All Rights Reserved.
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

from typing import List

from numpy import random

from common.python.utils import log_utils
from kernel.components.binning.horzfeaturebinning.horz_split_points import HorzFeatureBinningServer
from kernel.components.boosting import BoostingTree
from kernel.components.boosting.horzsecureboost.horz_decision_tree_arbiter import HorzDecisionTreeArbiter
from kernel.components.boosting.horzsecureboost.horz_secureboosting_aggregator import SecureBoostArbiterAggregator
from kernel.optimizer.convergence import converge_func_factory
from kernel.transfer.variables.transfer_class.horz_secure_boost_transfer_variable \
    import HorzSecureBoostingTransferVariable
from kernel.utils import consts

LOGGER = log_utils.get_logger()


class HorzSecureBoostingArbiter(BoostingTree):

    def __init__(self):
        super(HorzSecureBoostingArbiter, self).__init__()

        self.mode = consts.HORZ
        self.feature_num = 0
        self.role = consts.ARBITER
        self.transfer_inst = HorzSecureBoostingTransferVariable()
        self.check_convergence_func = None
        self.tree_dim = None
        self.aggregator = SecureBoostArbiterAggregator()
        self.global_loss_history = []

        # federated_binning obj
        self.binning_obj = HorzFeatureBinningServer()

    def sample_valid_feature(self):

        chosen_feature = random.choice(range(0, self.feature_num),
                                       max(1, int(self.subsample_feature_rate * self.feature_num)), replace=False)
        valid_features = [False for i in range(self.feature_num)]
        for fid in chosen_feature:
            valid_features[fid] = True

        return valid_features

    def sync_feature_num(self):
        feature_num_list = self.transfer_inst.feature_number.get(idx=-1, suffix=('feat_num',))
        for num in feature_num_list[1:]:
            assert feature_num_list[0] == num
        return feature_num_list[0]

    def sync_stop_flag(self, stop_flag, suffix):
        self.transfer_inst.stop_flag.remote(stop_flag, idx=-1, suffix=suffix)

    def sync_current_loss(self, suffix):
        loss_status_list = self.transfer_inst.loss_status.get(idx=-1, suffix=suffix)
        total_loss, total_num = 0, 0
        for l_ in loss_status_list:
            total_loss += l_['cur_loss'] * l_['sample_num']
            total_num += l_['sample_num']
        LOGGER.debug('loss status received, total_loss {}, total_num {}'.format(total_loss, total_num))
        return total_loss / total_num

    def sync_tree_dim(self):
        tree_dims = self.transfer_inst.tree_dim.get(idx=-1, suffix=('tree_dim',))
        dim0 = tree_dims[0]
        for dim in tree_dims[1:]:
            assert dim0 == dim
        return dim0

    def check_convergence(self, cur_loss):
        LOGGER.debug('checking convergence')
        return self.check_convergence_func.is_converge(cur_loss)

    def generate_flowid(self, round_num, tree_num):
        LOGGER.info("generate flowid, flowid {}".format(self.flowid))
        return ".".join(map(str, [self.flowid, round_num, tree_num]))

    def label_alignment(self) -> List:
        labels = self.transfer_inst.local_labels.get(idx=-1, suffix=('label_align',))
        label_set = set()
        for local_label in labels:
            label_set.update(local_label)
        global_label = list(label_set)
        global_label = sorted(global_label)
        label_mapping = {v: k for k, v in enumerate(global_label)}
        self.transfer_inst.label_mapping.remote(label_mapping, idx=-1, suffix=('label_mapping',))
        return label_mapping

    def federated_binning(self):
        self.binning_obj.average_run()

    def send_valid_features(self, valid_features, epoch_idx, t_idx):
        self.transfer_inst.valid_features.remote(valid_features, idx=-1, suffix=('valid_features', epoch_idx, t_idx))

    def fit(self, data_inst, valid_inst=None):

        self.federated_binning()
        # initializing
        self.feature_num = self.sync_feature_num()
        self.tree_dim = 1

        if self.task_type == consts.CLASSIFICATION:
            label_mapping = self.label_alignment()
            LOGGER.debug('label mapping is {}'.format(label_mapping))
            self.tree_dim = len(label_mapping) if len(label_mapping) > 2 else 1

        if self.n_iter_no_change:
            self.check_convergence_func = converge_func_factory("diff", self.tol)

        LOGGER.debug('begin to fit a boosting tree')
        for epoch_idx in range(self.num_trees):

            for t_idx in range(self.tree_dim):
                valid_feature = self.sample_valid_feature()
                self.send_valid_features(valid_feature, epoch_idx, t_idx)
                flow_id = self.generate_flowid(epoch_idx, t_idx)
                new_tree = HorzDecisionTreeArbiter(self.tree_param, valid_feature=valid_feature, epoch_idx=epoch_idx,
                                                   tree_idx=t_idx, flow_id=flow_id)
                new_tree.fit()

            global_loss = self.aggregator.aggregate_loss(suffix=(epoch_idx,))
            self.global_loss_history.append(global_loss)
            LOGGER.debug('cur epoch global loss is {},epoch_idx is {}'.format(global_loss, epoch_idx))

            metric_meta = {'abscissa_name': 'iters', 'ordinate_name': 'loss', 'metric_type': 'LOSS'}
            self.callback_metric(metric_name='loss',
                                 metric_namespace='train',
                                 metric_meta=metric_meta,
                                 metric_data=(epoch_idx, global_loss))

            if self.n_iter_no_change:
                should_stop = self.aggregator.broadcast_converge_status(self.check_convergence, (global_loss,),
                                                                        suffix=(epoch_idx,))
                LOGGER.debug('stop flag sent')
                if should_stop:
                    break

            self.tracker.add_task_progress(1)

        self.callback_metric("loss",
                             "train",
                             {'abscissa_name': 'iters', 'ordinate_name': 'loss', 'metric_type': 'LOSS'},
                             metric_data=("Best", min(self.global_loss_history)))

        LOGGER.debug('fitting horz decision tree done')

    def predict(self, data_inst):

        LOGGER.debug('start predicting')

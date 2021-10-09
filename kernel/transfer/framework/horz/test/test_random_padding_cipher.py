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

from kernel.transfer.framework.horz.procedure import random_padding_cipher
from kernel.utils import consts
from .horz_test_sync_base import TestSyncBase


class RandomPaddingCipherTest(TestSyncBase):
    @classmethod
    def call(cls, role, transfer_variable, ind, *args):
        if role == consts.ARBITER:
            rp_cipher = random_padding_cipher.Arbiter()
            rp_cipher.register_random_padding_cipher(transfer_variable)
            rp_cipher.exchange_secret_keys()
            return

        elif role == consts.PROVIDER:
            rp_cipher = random_padding_cipher.Provider()
            rp_cipher.register_random_padding_cipher(transfer_variable)
            rp_cipher.create_cipher()
            return rp_cipher
        else:
            rp_cipher = random_padding_cipher.Promoter()
            rp_cipher.register_random_padding_cipher(transfer_variable)
            rp_cipher.create_cipher()
            return rp_cipher

    def run_with_num_hosts(self, num_hosts):

        arbiter, promoter, *hosts = self.run_results(num_hosts)
        import numpy as np
        raw = np.zeros((10, 10))
        encrypted = np.zeros((10, 10))

        promoter_matrix = np.random.rand(10, 10)
        raw += promoter_matrix
        encrypted += promoter.encrypt(promoter_matrix)

        for host in hosts:
            host_matrix = np.random.rand(10, 10)
            raw += host_matrix
            encrypted += host.encrypt(host_matrix)

        self.assertTrue(np.allclose(raw, encrypted))

    def test_host_1(self):
        self.run_with_num_hosts(1)

    def test_host_10(self):
        self.run_with_num_hosts(10)

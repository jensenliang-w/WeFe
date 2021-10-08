#!/usr/bin/env python
# -*- coding: utf-8 -*-

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



################################################################################
#
# AUTO GENERATED TRANSFER VARIABLE CLASS. DO NOT MODIFY
#
################################################################################

from kernel.transfer.variables.base_transfer_variable import BaseTransferVariables


# noinspection PyAttributeOutsideInit
class LegacyAggregatorTransVar(BaseTransferVariables):
    def __init__(self, flowid=0):
        super().__init__(flowid)
        self.HasConvergedTransVar.has_converged = self._create_variable(name='HasConvergedTransVar.has_converged')
        self.LossScatterTransVar.loss = self._create_variable(name='LossScatterTransVar.loss')
        self.ModelBroadcasterTransVar.server_model = self._create_variable(name='ModelBroadcasterTransVar.server_model')
        self.ModelScatterTransVar.client_model = self._create_variable(name='ModelScatterTransVar.client_model')
        self.RandomPaddingCipherTransVar.DHTransVar.p_power_r = self._create_variable(
            name='RandomPaddingCipherTransVar.DHTransVar.p_power_r')
        self.RandomPaddingCipherTransVar.DHTransVar.p_power_r_bc = self._create_variable(
            name='RandomPaddingCipherTransVar.DHTransVar.p_power_r_bc')
        self.RandomPaddingCipherTransVar.DHTransVar.pubkey = self._create_variable(
            name='RandomPaddingCipherTransVar.DHTransVar.pubkey')
        self.RandomPaddingCipherTransVar.UUIDTransVar.uuid = self._create_variable(
            name='RandomPaddingCipherTransVar.UUIDTransVar.uuid')

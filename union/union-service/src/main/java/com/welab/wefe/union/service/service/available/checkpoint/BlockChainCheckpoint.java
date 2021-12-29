/*
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.welab.wefe.union.service.service.available.checkpoint;

import com.welab.wefe.common.wefe.checkpoint.AbstractCheckpoint;
import com.welab.wefe.common.wefe.enums.ServiceType;
import com.welab.wefe.union.service.service.MemberContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zane
 * @date 2021/12/21
 */
@Service
public class BlockChainCheckpoint extends AbstractCheckpoint {

    @Autowired
    private MemberContractService memberContractService;

    @Override
    protected ServiceType service() {
        return ServiceType.BlockChainService;
    }

    @Override
    protected String desc() {
        return "检查区块链服务是否运行良好";
    }

    @Override
    protected String getConfigValue() {
        return null;
    }

    @Override
    protected String messageWhenConfigValueEmpty() {
        return null;
    }

    @Override
    protected void doCheck(String value) throws Exception {
        memberContractService.isExist("test_member_id");
    }
}

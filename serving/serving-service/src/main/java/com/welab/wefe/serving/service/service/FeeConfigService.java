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

package com.welab.wefe.serving.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.welab.wefe.common.data.mysql.Where;
import com.welab.wefe.common.data.mysql.enums.OrderBy;
import com.welab.wefe.common.util.StringUtil;
import com.welab.wefe.serving.service.api.feeconfig.SaveApi;
import com.welab.wefe.serving.service.database.serving.entity.FeeConfigMysqlModel;
import com.welab.wefe.serving.service.database.serving.repository.FeeConfigRepository;

/**
 * @author ivenn.zheng
 */
@Service
public class FeeConfigService {


    @Autowired
    private FeeConfigRepository feeConfigRepository;


    public FeeConfigMysqlModel save(SaveApi.Input input) {

        FeeConfigMysqlModel model = feeConfigRepository.findOne("id", input.getId(), FeeConfigMysqlModel.class);

        if (null == model) {
            model = new FeeConfigMysqlModel();

        }

        if (null != input.getUnitPrice()) {
            model.setUnitPrice(input.getUnitPrice());
        }
        // 预留字段
        if (null != input.getPayType()) {
            model.setPayType(input.getPayType());
        }
        return feeConfigRepository.save(model);
    }


    public FeeConfigMysqlModel queryOne(String serviceId, String clientId) {
        if (StringUtil.isNotEmpty(serviceId) && StringUtil.isNotEmpty(clientId)) {
            Specification<FeeConfigMysqlModel> where = Where
                    .create()
                    .equal("serviceId", serviceId)
                    .equal("clientId", clientId)
                    .orderBy("createdTime", OrderBy.desc)
                    .build(FeeConfigMysqlModel.class);
            // 返回最新的计费规则配置（因为一个客户服务可能存在多个计费规则）
            List<FeeConfigMysqlModel> all = feeConfigRepository.findAll(where);
            return all.get(0);
        }

        return null;
    }
}

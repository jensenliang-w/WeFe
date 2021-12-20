/**
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welab.wefe.board.service.service.available.checkpoint;

import com.welab.wefe.board.service.constant.Config;
import com.welab.wefe.board.service.sdk.UnionService;
import com.welab.wefe.common.wefe.checkpoint.AbstractCheckpoint;
import com.welab.wefe.common.wefe.enums.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zane
 */
@Service
public class UnionConnectionCheckpoint extends AbstractCheckpoint {
    @Autowired
    protected Config config;

    @Autowired
    private UnionService unionService;

    @Override
    public ServiceType service() {
        return ServiceType.UnionService;
    }

    @Override
    public String desc() {
        return "检查 board 与 union 服务的连通性";
    }

    @Override
    public String value() {
        return config.getUNION_BASE_URL();
    }

    @Override
    protected void doCheck() throws Exception {
        unionService.request("union/available", null);
    }
}

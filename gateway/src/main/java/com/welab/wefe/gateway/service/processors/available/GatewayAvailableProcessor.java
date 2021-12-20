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

package com.welab.wefe.gateway.service.processors.available;

import com.welab.wefe.common.util.JObject;
import com.welab.wefe.common.util.StringUtil;
import com.welab.wefe.common.wefe.checkpoint.AbstractCheckpoint;
import com.welab.wefe.common.wefe.checkpoint.dto.ServerAvailableCheckOutput;
import com.welab.wefe.common.wefe.checkpoint.dto.ServerCheckPointOutput;
import com.welab.wefe.gateway.GatewayServer;
import com.welab.wefe.gateway.api.meta.basic.BasicMetaProto;
import com.welab.wefe.gateway.api.meta.basic.GatewayMetaProto;
import com.welab.wefe.gateway.base.Processor;
import com.welab.wefe.gateway.common.ReturnStatusBuilder;
import com.welab.wefe.gateway.service.processors.AbstractProcessor;
import com.welab.wefe.gateway.service.processors.available.checkpoint.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gateway availability processor
 * <p>
 * For example, whether the gateway can be connected to MySQL, storage, union, etc
 * </p>
 *
 * @author aaron.li
 **/
@Processor(name = "gatewayAvailableProcessor", desc = "Gateway availability processor")
public class GatewayAvailableProcessor extends AbstractProcessor {
    private final Logger LOG = LoggerFactory.getLogger(GatewayAvailableProcessor.class);

    @Override
    public BasicMetaProto.ReturnStatus beforeSendToRemote(GatewayMetaProto.TransferMeta transferMeta) {
        // Check self
        if (isCheckSelf(transferMeta)) {
            ServerAvailableCheckOutput result = checkService();
            return ReturnStatusBuilder.ok(transferMeta.getSessionId(), JObject.create(result).toJSONString());
        }

        return super.remoteProcess(transferMeta);
    }

    @Override
    public BasicMetaProto.ReturnStatus remoteProcess(GatewayMetaProto.TransferMeta transferMeta) {
        ServerAvailableCheckOutput result = checkService();
        result.cleanValues();
        return ReturnStatusBuilder.ok(transferMeta.getSessionId(), JObject.create(result).toJSONString());
    }

    private static final List<Class<? extends AbstractCheckpoint>> CHECKPOINT_LIST = Arrays.asList(
            MysqlCheckpoint.class,
            StorageCheckpoint.class,
            UnionCheckpoint.class,
            BoardCheckpoint.class,
            FileSystemCheckpoint.class
    );


    /**
     * Check whether relevant services are normal
     *
     * @return check result list
     */
    private ServerAvailableCheckOutput checkService() {
        List<ServerCheckPointOutput> list = new ArrayList<>();

        for (Class<? extends AbstractCheckpoint> clazz : CHECKPOINT_LIST) {
            AbstractCheckpoint checkpoint = GatewayServer.CONTEXT.getBean(clazz);
            ServerCheckPointOutput result = checkpoint.check();
            list.add(result);
        }

        return new ServerAvailableCheckOutput(list);
    }


    /**
     * Is check self availability
     */
    private boolean isCheckSelf(GatewayMetaProto.TransferMeta transferMeta) {
        GatewayMetaProto.Member srcMember = transferMeta.getSrc();
        GatewayMetaProto.Member dstMember = transferMeta.getDst();
        return StringUtil.isEmpty(dstMember.getMemberId()) || srcMember.getMemberId().equals(dstMember.getMemberId());
    }


}

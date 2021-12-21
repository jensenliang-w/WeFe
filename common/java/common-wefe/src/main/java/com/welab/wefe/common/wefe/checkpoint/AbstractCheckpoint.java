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

package com.welab.wefe.common.wefe.checkpoint;

import com.welab.wefe.common.CommonThreadPool;
import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.wefe.checkpoint.dto.ServerCheckPointOutput;
import com.welab.wefe.common.wefe.enums.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author zane
 */
public abstract class AbstractCheckpoint {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected abstract ServiceType service();

    protected abstract String desc();

    protected abstract String value();

    protected abstract void doCheck() throws Exception;

    public ServerCheckPointOutput check() {
        long start = System.currentTimeMillis();

        String value = null;
        try {
            value = value();
        } catch (Exception e) {
            throw new RuntimeException("获取 value 失败，" + e.getClass().getSimpleName() + ":" + e.getMessage());
        }

        String finalValue = value;
        Future<Exception> future = CommonThreadPool.submit(() -> {
            try {


                if (finalValue == null) {
                    StatusCode
                            .PARAMETER_CAN_NOT_BE_EMPTY
                            .throwException("相关配置为空，请进行设置后再进行检查。");
                }
                doCheck();
            } catch (Exception e) {
                return e;
            }
            return null;
        });

        Exception e;
        try {
            e = future.get(5, TimeUnit.SECONDS);
        } catch (Exception ex) {
            e = ex;
        }

        ServerCheckPointOutput output = new ServerCheckPointOutput();
        output.setService(service());
        output.setDesc(desc());
        output.setValue(value);
        output.setSpend(System.currentTimeMillis() - start);

        if (e == null) {
            output.setSuccess(true);
            output.setMessage("success");
        } else {
            output.setSuccess(false);
            output.setMessage(e.getMessage());
        }
        return output;
    }

    protected void log(Exception e) {
        LOG.error(e.getClass() + " " + e.getMessage(), e);
    }
}

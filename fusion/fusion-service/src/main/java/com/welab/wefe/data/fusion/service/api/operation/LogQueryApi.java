/*
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welab.wefe.data.fusion.service.api.operation;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.data.fusion.service.dto.base.PagingInput;
import com.welab.wefe.data.fusion.service.dto.base.PagingOutput;
import com.welab.wefe.data.fusion.service.dto.entity.OperationLogOutputModel;
import com.welab.wefe.data.fusion.service.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author eval
 **/
@Api(path = "log/query", name = "query log")
public class LogQueryApi extends AbstractApi<LogQueryApi.Input, PagingOutput<OperationLogOutputModel>> {

    @Autowired
    OperationLogService service;

    @Override
    protected ApiResult<PagingOutput<OperationLogOutputModel>> handle(Input input) throws StatusCodeWithException {
        return success(service.query(input));
    }

    public static class Input extends PagingInput {
        private String apiName;
        private String nickname;
        private Long startTime;
        private Long endTime;

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }


        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}

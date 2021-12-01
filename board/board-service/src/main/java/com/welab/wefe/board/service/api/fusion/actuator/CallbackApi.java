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

package com.welab.wefe.board.service.api.fusion.actuator;

import com.welab.wefe.board.service.service.fusion.CallbackService;
import com.welab.wefe.common.enums.AuditStatus;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractNoneOutputApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hunter.zhao
 */
@Api(path = "fusion/callback", name = "接收消息接口", rsaVerify = true
)
public class CallbackApi extends AbstractNoneOutputApi<CallbackApi.Input> {
    @Autowired
    CallbackService callbackService;

    @Override
    protected ApiResult handler(Input input) throws StatusCodeWithException {
        callbackService.callback(input);
        return success();
    }


    public static class Input extends AbstractApiInput {

        @Check(name = "指定操作的projectId", require = true)
        private String projectId;

        @Check(name = "指定操作的businessId", require = true)
        private String businessId;

//        @Check(name = "消息类型", require = true)
//        private CallbackType type;

        @Check(name = "审核字段", require = true)
        private AuditStatus auditStatus;

        @Check(name = "审核评论")
        private String auditComment;

        public String getBusinessId() {
            return businessId;
        }

        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public AuditStatus getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(AuditStatus auditStatus) {
            this.auditStatus = auditStatus;
        }

        public String getAuditComment() {
            return auditComment;
        }

        public void setAuditComment(String auditComment) {
            this.auditComment = auditComment;
        }
    }
}

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

package com.welab.wefe.union.service.api.member;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.union.service.dto.base.BaseInput;
import com.welab.wefe.union.service.dto.member.MemberOutput;
import com.welab.wefe.union.service.service.MemberContractService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author aaron.li
 **/
@Api(path = "member/update_last_activity_time", name = "member_update_last_activity_time", rsaVerify = true, login = false)
public class UpdateLastActivityTimeApi extends AbstractApi<UpdateLastActivityTimeApi.Input, MemberOutput> {
    @Autowired
    private MemberContractService memberContractService;


    @Override
    protected ApiResult<MemberOutput> handle(UpdateLastActivityTimeApi.Input input) throws StatusCodeWithException {
        memberContractService.updateLastActivityTimeById(input.curMemberId, String.valueOf(input.lastActivityTime));
        return success();
    }

    public static class Input extends BaseInput {
        @Check(require = true)
        private String id;
        @Check(require = true)
        private long lastActivityTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getLastActivityTime() {
            return lastActivityTime;
        }

        public void setLastActivityTime(long lastActivityTime) {
            this.lastActivityTime = lastActivityTime;
        }
    }
}

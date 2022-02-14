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

package com.welab.wefe.board.service.api.data_resource.bloom_filter;


import com.welab.wefe.board.service.dto.entity.BloomFilterDataResourceListOutputModel;
import com.welab.wefe.board.service.service.data_resource.bloom_filter.BloomFilterService;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author jacky.jiang
 */
@Api(path = "data_resource/member/query", name = "query data_resource")
public class BloomFilterDataResourceListApi extends AbstractApi<BloomFilterDataResourceListApi.Input, BloomFilterDataResourceListOutputModel> {

    @Autowired
    private BloomFilterService bloomfilterService;

    @Override
    protected ApiResult<BloomFilterDataResourceListOutputModel> handle(Input input) throws StatusCodeWithException, IOException {
        return success(bloomfilterService.query(input));
    }

    public static class Input extends AbstractApiInput {
        @Check(name = "工程 Id", require = true)
        private String projectId;

        @Check(name = "成员 Id", require = true)
        private String memberId;

        @Check(name = "成员类型", require = true)
        private String role;

        //region getter/setter


        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        //endregion
    }
}

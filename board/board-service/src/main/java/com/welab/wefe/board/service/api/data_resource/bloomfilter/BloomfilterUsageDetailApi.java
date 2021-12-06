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

package com.welab.wefe.board.service.api.data_resource.bloomfilter;

import com.welab.wefe.board.service.dto.entity.project.ProjectUsageDetailOutputModel;
import com.welab.wefe.board.service.service.data_resource.DataResourceService;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @author Jacky.jiang
 */
@Api(path = "bloomfilter/usage_detail", name = "list usage_detail")
public class BloomfilterUsageDetailApi extends AbstractApi<BloomfilterUsageDetailApi.Input, List<ProjectUsageDetailOutputModel>> {
    @Autowired
    private DataResourceService dataResourceService;

    @Override
    protected ApiResult<List<ProjectUsageDetailOutputModel>> handle(Input input) throws StatusCodeWithException, IOException {
        return success(dataResourceService.queryUsageInProject(input.getBloomfilterId()));
    }

    public static class Input extends AbstractApiInput {
        @Check(name = "过滤器ID", require = true)
        private String bloomfilterId;

        //region getter/setter

        public String getBloomfilterId() {
            return bloomfilterId;
        }

        public void setBloomfilterId(String bloomfilterId) {
            this.bloomfilterId = bloomfilterId;
        }


        //endregion
    }
}

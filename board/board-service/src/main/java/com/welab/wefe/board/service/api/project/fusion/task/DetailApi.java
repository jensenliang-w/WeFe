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

package com.welab.wefe.board.service.api.project.fusion.task;

import com.welab.wefe.board.service.dto.fusion.FusionTaskOutput;
import com.welab.wefe.board.service.service.fusion.FusionTaskService;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hunter.zhao
 */
@Api(path = "fusion/task/detail", name = "任务列表", desc = "任务列表", login = false)
public class DetailApi extends AbstractApi<DetailApi.Input, FusionTaskOutput> {
    @Autowired
    FusionTaskService fusionTaskService;

    @Override
    protected ApiResult<FusionTaskOutput> handle(Input input) throws StatusCodeWithException {
        return success(fusionTaskService.detail(input.id));
    }

    public static class Input extends AbstractApiInput {
        @Check(name = "指定操作的taskId", require = true)
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

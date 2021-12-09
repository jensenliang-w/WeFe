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

package com.welab.wefe.board.service.api.data_resource;

import com.welab.wefe.board.service.dto.base.PagingInput;
import com.welab.wefe.board.service.service.data_resource.DataResourceService;
import com.welab.wefe.board.service.service.data_resource.image_data_set.ImageDataSetSampleService;
import com.welab.wefe.board.service.service.data_resource.image_data_set.ImageDataSetService;
import com.welab.wefe.common.enums.DataResourceType;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Zane
 */
@Api(path = "data_resource/query", name = "query all kinds of data resource")
public class DataResourceQueryApi extends AbstractApi<DataResourceQueryApi.Input, String> {

    @Autowired
    private ImageDataSetService imageDataSetService;
    @Autowired
    private ImageDataSetSampleService imageDataSetSampleService;
    @Autowired
    private DataResourceService dataResourceService;

    @Override
    protected ApiResult<String> handle(Input input) throws StatusCodeWithException {
        return null;

    }

    public static class Input extends PagingInput {
        @Check(name = "资源Id")
        private String id;
        @Check(name = "过滤器名称")
        private String name;
        @Check(name = "标签")
        private String tag;
        @Check(name = "上传者")
        private String creator;
        @Check(name = "资源类型")
        private DataResourceType dataResourceType;

        // region getter/setter

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public DataResourceType getDataResourceType() {
            return dataResourceType;
        }

        public void setDataResourceType(DataResourceType dataResourceType) {
            this.dataResourceType = dataResourceType;
        }


        // endregion
    }
}

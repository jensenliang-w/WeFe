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

package com.welab.wefe.board.service.api.data_source.image_data_set.sample;

import com.welab.wefe.board.service.service.dataset.ImageDataSetSampleService;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractNoneOutputApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Zane
 */
@Api(path = "image_data_set_sample/delete", name = "delete image data set sample", login = false)
public class ImageDataSetSampleDeleteApi extends AbstractNoneOutputApi<ImageDataSetSampleDeleteApi.Input> {

    @Autowired
    private ImageDataSetSampleService imageDataSetSampleService;

    @Override
    protected ApiResult<?> handler(Input input) throws StatusCodeWithException {
        imageDataSetSampleService.delete(input.id);

        return success();
    }

    public static class Input extends AbstractApiInput {
        @Check(require = true)
        public String id;
    }
}

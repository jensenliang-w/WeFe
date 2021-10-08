/**
 * Copyright 2021 The WeFe Authors. All Rights Reserved.
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

package com.welab.wefe.board.service.api.union;

import com.alibaba.fastjson.JSONObject;
import com.welab.wefe.board.service.dto.base.PagingInput;
import com.welab.wefe.board.service.sdk.UnionService;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lonnie
 */
@Api(path = "union/tag/query", name = "Get the default dataset tags from union")
public class TagListApi extends AbstractApi<TagListApi.Input, JSONObject> {

    @Autowired
    UnionService unionService;

    @Override
    protected ApiResult<JSONObject> handle(Input input) throws StatusCodeWithException {
        JSONObject result = unionService.queryTags(input);
        return unionApiResultToBoardApiResult(result);
    }

    public static class Input extends PagingInput {
    }
}

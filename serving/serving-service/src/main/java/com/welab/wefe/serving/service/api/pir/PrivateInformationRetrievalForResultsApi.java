/**
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

package com.welab.wefe.serving.service.api.pir;

import java.io.IOException;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.mpc.pir.PrivateInformationRetrievalApiName;
import com.welab.wefe.mpc.pir.request.QueryResultsRequest;
import com.welab.wefe.mpc.pir.request.QueryResultsResponse;
import com.welab.wefe.mpc.pir.server.service.HuackResultsService;
import com.welab.wefe.serving.service.utils.ModelMapper;

@Api(path = PrivateInformationRetrievalApiName.RESULTS, name = "results")
public class PrivateInformationRetrievalForResultsApi
		extends AbstractApi<PrivateInformationRetrievalForResultsApi.Input, QueryResultsResponse> {

	@Override
	protected ApiResult<QueryResultsResponse> handle(Input input) throws StatusCodeWithException, IOException {
		HuackResultsService service = new HuackResultsService();
		QueryResultsRequest request = ModelMapper.map(input, QueryResultsRequest.class);
		return success(service.handle(request));
	}

	public static class Input extends AbstractApiInput {

		private String uuid;

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
	}

}

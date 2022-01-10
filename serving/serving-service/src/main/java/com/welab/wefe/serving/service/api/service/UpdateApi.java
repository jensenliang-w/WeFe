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

package com.welab.wefe.serving.service.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.serving.service.api.service.AddApi.Output;
import com.welab.wefe.serving.service.service.ServiceService;

@Api(path = "service/update", name = "update service info")
public class UpdateApi extends AbstractApi<UpdateApi.Input, AddApi.Output> {

	@Autowired
	private ServiceService service;

	@Override
	protected ApiResult<Output> handle(Input input) throws StatusCodeWithException, IOException {
		return success(service.update(input));
	}

	public static class Input extends AbstractApiInput {

		@Check(require = true, name = "ID")
		private String id;
		@Check(name = "服务名")
		private String name;
		@Check(name = "服务地址")
		private String url;
		@Check(name = "服务类型")
		private int serviceType;
		@Check(name = "查询参数配置")
		private List<String> queryParams;// json
		@Check(name = "SQL配置")
		private String dataSource;// json
		@Check(name = "服务配置")
		private String serviceConfig;// json

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

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getServiceType() {
			return serviceType;
		}

		public void setServiceType(int serviceType) {
			this.serviceType = serviceType;
		}

		public List<String> getQueryParams() {
			return queryParams;
		}

		public void setQueryParams(List<String> queryParams) {
			this.queryParams = queryParams;
		}

		public String getDataSource() {
			return dataSource;
		}

		public void setDataSource(String dataSource) {
			this.dataSource = dataSource;
		}

		public String getServiceConfig() {
			return serviceConfig;
		}

		public void setServiceConfig(String serviceConfig) {
			this.serviceConfig = serviceConfig;
		}
	}

}

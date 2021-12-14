package com.welab.wefe.serving.service.api.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiOutput;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.serving.service.dto.PagingInput;
import com.welab.wefe.serving.service.dto.PagingOutput;
import com.welab.wefe.serving.service.service.ServiceService;

@Api(path = "service/query", name = "query service list")
public class QueryApi extends AbstractApi<QueryApi.Input, PagingOutput<QueryApi.Output>> {

	@Autowired
	ServiceService serviceService;

	@Override
	protected ApiResult<PagingOutput<Output>> handle(Input input) throws StatusCodeWithException, IOException {
		return success(serviceService.query(input));
	}

	public static class Output extends AbstractApiOutput {
		private String id;
		private String name;
		private int serviceType;
		private int status;
		private Date createdTime;
		private Date updatedTime;

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

		public int getServiceType() {
			return serviceType;
		}

		public void setServiceType(int serviceType) {
			this.serviceType = serviceType;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public Date getCreatedTime() {
			return createdTime;
		}

		public void setCreatedTime(Date createdTime) {
			this.createdTime = createdTime;
		}

		public Date getUpdatedTime() {
			return updatedTime;
		}

		public void setUpdatedTime(Date updatedTime) {
			this.updatedTime = updatedTime;
		}

	}

	public static class Input extends PagingInput {

		// 服务名
		private String name;

		// 服务类型 1=匿踪查询，2=交集查询，3=安全聚合
		private int serviceType;

		// 是否在线 1=在线 0=离线
		private int status;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getServiceType() {
			return serviceType;
		}

		public void setServiceType(int serviceType) {
			this.serviceType = serviceType;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

	}
}

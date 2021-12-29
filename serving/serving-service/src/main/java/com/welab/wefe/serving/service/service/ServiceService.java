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

package com.welab.wefe.serving.service.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.data.mysql.Where;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.JObject;
import com.welab.wefe.common.web.CurrentAccount;
import com.welab.wefe.mpc.cache.result.QueryDataResult;
import com.welab.wefe.mpc.cache.result.QueryDataResultFactory;
import com.welab.wefe.mpc.pir.request.QueryKeysRequest;
import com.welab.wefe.mpc.pir.request.QueryKeysResponse;
import com.welab.wefe.mpc.pir.server.service.HuackKeyService;
import com.welab.wefe.serving.service.api.service.AddApi;
import com.welab.wefe.serving.service.api.service.QueryApi;
import com.welab.wefe.serving.service.api.service.ServiceSQLTestApi.Output;
import com.welab.wefe.serving.service.api.service.UpdateApi.Input;
import com.welab.wefe.serving.service.database.serving.entity.ServiceMySqlModel;
import com.welab.wefe.serving.service.database.serving.repository.ServiceRepository;
import com.welab.wefe.serving.service.dto.PagingOutput;
import com.welab.wefe.serving.service.utils.ModelMapper;
import com.welab.wefe.serving.service.utils.ServiceUtil;
import com.welab.wefe.serving.service.utils.ZipUtils;

/**
 * 服务 Service
 */
@Service
public class ServiceService {

	public static final String SERVICE_PRE_URL = "api/";
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private DataSourceService dataSourceService;

	@Transactional(rollbackFor = Exception.class)
	public com.welab.wefe.serving.service.api.service.AddApi.Output save(AddApi.Input input)
			throws StatusCodeWithException {
		ServiceMySqlModel model = serviceRepository.findOne("url", input.getUrl(), ServiceMySqlModel.class);
		if (model != null) {
			throw new StatusCodeWithException(StatusCode.PRIMARY_KEY_CONFLICT, input.getUrl(), "url");
		}
		model = ModelMapper.map(input, ServiceMySqlModel.class);
		model.setCreatedBy(CurrentAccount.id());
		model.setCreatedTime(new Date());
		model.setUpdatedBy(CurrentAccount.id());
		model.setUpdatedTime(new Date());
		serviceRepository.save(model);

		com.welab.wefe.serving.service.api.service.AddApi.Output output = new com.welab.wefe.serving.service.api.service.AddApi.Output();
		output.setId(model.getId());
		output.setParams(model.getQueryParams());
		output.setUrl(SERVICE_PRE_URL + model.getUrl());
		return output;
	}

	/**
	 * Paging query
	 */
	public PagingOutput<QueryApi.Output> query(QueryApi.Input input) {

		Where where = Where.create();
		if (StringUtils.isNotBlank(input.getId())) {
			where = where.equal("id", input.getId());
		}
		if (StringUtils.isNotBlank(input.getName())) {
			where = where.equal("name", input.getName());
		}
		if (input.getServiceType() != -1) {
			where = where.equal("serviceType", input.getServiceType());
		}
		if (input.getStatus() != -1) {
			where = where.equal("status", input.getStatus());
		}
		Specification<ServiceMySqlModel> condition = where.build(ServiceMySqlModel.class);

		PagingOutput<ServiceMySqlModel> page = serviceRepository.paging(condition, input);

		List<QueryApi.Output> list = page.getList().stream().map(x -> ModelMapper.map(x, QueryApi.Output.class))
				.collect(Collectors.toList());

		return PagingOutput.of(page.getTotal(), list);
	}

	public void update(Input input) throws StatusCodeWithException {
		ServiceMySqlModel model = serviceRepository.findOne("id", input.getId(), ServiceMySqlModel.class);
		if (model == null) {
			throw new StatusCodeWithException(StatusCode.PARAMETER_VALUE_INVALID, "entity not exists");
		}
		if (StringUtils.isNotBlank(input.getName())) {
			model.setName(input.getName());
		}
		if (StringUtils.isNotBlank(input.getUrl())) {
			model.setUrl(input.getUrl());
		}
		if (StringUtils.isNotBlank(input.getQueryParams())) {
			model.setQueryParams(input.getQueryParams());
		}
		if (StringUtils.isNotBlank(input.getDataSource())) {
			model.setDataSource(input.getDataSource());
		}
		if (input.getServiceType() != -1) {
			model.setServiceType(input.getServiceType());
		}
		serviceRepository.save(model);
	}

	public void offlineService(String id) throws StatusCodeWithException {
		ServiceMySqlModel model = serviceRepository.findOne("id", id, ServiceMySqlModel.class);
		if (model == null) {
			throw new StatusCodeWithException(StatusCode.PARAMETER_VALUE_INVALID, "服务不存在");
		}
		if (model.getStatus() == 0) {
			throw new StatusCodeWithException(StatusCode.PARAMETER_VALUE_INVALID, "服务已是下线状态，无需重复操作");
		}
		model.setStatus(0);
		serviceRepository.save(model);
	}

	public void onlineService(String id) throws StatusCodeWithException {
		ServiceMySqlModel model = serviceRepository.findOne("id", id, ServiceMySqlModel.class);
		if (model == null) {
			throw new StatusCodeWithException(StatusCode.PARAMETER_VALUE_INVALID, "服务不存在");
		}
		if (model.getStatus() == 1) {
			throw new StatusCodeWithException(StatusCode.PARAMETER_VALUE_INVALID, "服务已是在线状态，无需重复操作");
		}
		model.setStatus(1);
		serviceRepository.save(model);
	}

	public Output sqlTest(com.welab.wefe.serving.service.api.service.ServiceSQLTestApi.Input input)
			throws StatusCodeWithException {
		int index = 0;
		JSONArray dataSourceArr = JObject.parseArray(input.getDataSource());
		String resultfields = ServiceUtil.parseReturnFields(dataSourceArr, index);
		String dataSourceId = dataSourceArr.getJSONObject(index).getString("id");
		String sql = ServiceUtil.generateSQL(input.getParams(), dataSourceArr, index);
		Map<String, String> result = dataSourceService.execute(dataSourceId, sql,
				Arrays.asList(resultfields.split(",")));
		Output out = new Output();
		out.setResult(JObject.create(result));
		return out;
	}

	public com.welab.wefe.serving.service.api.service.RouteApi.Output executeService(String serviceUrl,
			com.welab.wefe.serving.service.api.service.RouteApi.Input input) throws StatusCodeWithException {
		ServiceMySqlModel model = serviceRepository.findOne("url", serviceUrl, ServiceMySqlModel.class);
		com.welab.wefe.serving.service.api.service.RouteApi.Output output = new com.welab.wefe.serving.service.api.service.RouteApi.Output();
		if (model == null) {
			output.setCode(-1);
			output.setMessage("invalid request");
			return output;
		} else {
			int serviceType = model.getServiceType();// 服务类型 1匿踪查询，2交集查询，3安全聚合

			if (serviceType == 1) {// 1匿踪查询
				List<String> ids = input.getIds();
				QueryKeysResponse result = pir(ids, model);
				output.setCode(0);
				output.setMessage("success");
				output.setResult((JSONObject)JObject.toJSON(result));
				return output;
			} else if (serviceType == 2) {// 2交集查询

			} else if (serviceType == 3) {// 3安全聚合
				
			}
			output.setCode(0);
			output.setMessage("success");
			return output;
		}
	}

	private QueryKeysResponse pir(List<String> ids, ServiceMySqlModel model) throws StatusCodeWithException {
		Map<String, String> result = new HashMap<>();
		// 0 根据ID查询对应的数据
		for (String id : ids) {// params
			JSONArray dataSourceArr = JObject.parseArray(model.getDataSource());
			int index = 0;
			String sql = ServiceUtil.generateSQL(id, dataSourceArr, index);
			String dataSourceId = dataSourceArr.getJSONObject(index).getString("id");
			String resultfields = ServiceUtil.parseReturnFields(dataSourceArr, index);
			try {
				Map<String, String> resultMap = dataSourceService.execute(dataSourceId, sql,
						Arrays.asList(resultfields.split(",")));
				if(resultMap == null || resultMap.isEmpty()) {
					resultMap =  new HashMap<>();
					resultMap.put("rand", "thisisrandomstring");
				}
				String resultStr = JObject.toJSONString(resultMap);
				System.out.println(id + "\t " + resultStr);
				result.put(id, resultStr);
			} catch (StatusCodeWithException e) {
				throw e;
			}
		}
		QueryKeysRequest request = new QueryKeysRequest();
		request.setIds((List) ids);
		request.setMethod("plain");
		HuackKeyService service = new HuackKeyService();
		String uuid = "";
		QueryKeysResponse response = null;
		try {
			response = service.handle(request);
			// 3 取出 QueryKeysResponse 的uuid
			// 将uuid传入QueryResult
			uuid = response.getUuid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new StatusCodeWithException(StatusCode.SYSTEM_ERROR, "系统异常，请联系管理员");
		}
		// 将 0 步骤查询的数据 保存到 QueryResult -> LocalResultCache
		QueryDataResult<Map<String, String>> queryResult = QueryDataResultFactory.getQueryDataResult();
		queryResult.save(uuid, result);
		return response;
	}

	public ResponseEntity<byte[]> exportSdk(String serviceId) throws StatusCodeWithException, FileNotFoundException {
		ServiceMySqlModel model = serviceRepository.findOne("id", serviceId, ServiceMySqlModel.class);
		if (model == null) {
			throw new StatusCodeWithException(StatusCode.PARAMETER_VALUE_INVALID, "service not exists");
		}
		int serviceType = model.getServiceType();// 服务类型 1匿踪查询，2交集查询，3安全聚合
		String projectPath = System.getProperty("user.dir");
		String outputPath = "";
		List<File> fileList = new ArrayList<>();
		String sdkZipName = "";
		if (serviceType == 1) {
			sdkZipName = "sdk.zip";
			outputPath = projectPath + "/sdk_dir/" + sdkZipName;
			// TODO 将需要提供的文件加到这个列表
			fileList.add(new File(projectPath + "/sdk_dir/mpc-pir-sdk-1.0.0.jar"));
			fileList.add(new File(projectPath + "/sdk_dir/readme.md"));
		}
		FileOutputStream fos2 = new FileOutputStream(new File(outputPath));
		ZipUtils.toZip(fileList, fos2);
		File file = new File(outputPath);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", sdkZipName);
		try {
			return new ResponseEntity<>(ServiceUtil.fileToBytes(file), headers, HttpStatus.CREATED);
		} catch (IOException e) {
			e.printStackTrace();
			throw new StatusCodeWithException(StatusCode.SYSTEM_ERROR, "系统异常，请联系管理员");
		}
	}
}

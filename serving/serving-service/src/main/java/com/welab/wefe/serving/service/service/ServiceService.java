package com.welab.wefe.serving.service.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.data.mysql.Where;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.JObject;
import com.welab.wefe.common.web.CurrentAccount;
import com.welab.wefe.serving.service.api.service.AddApi;
import com.welab.wefe.serving.service.api.service.QueryApi;
import com.welab.wefe.serving.service.api.service.ServiceSQLTestApi.Output;
import com.welab.wefe.serving.service.api.service.UpdateApi.Input;
import com.welab.wefe.serving.service.database.serving.entity.ServiceMySqlModel;
import com.welab.wefe.serving.service.database.serving.repository.ServiceRepository;
import com.welab.wefe.serving.service.dto.PagingOutput;
import com.welab.wefe.serving.service.utils.ModelMapper;

/**
 * 服务 Service
 */
@Service
public class ServiceService {

	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private DataSourceService dataSourceService;

	@Transactional(rollbackFor = Exception.class)
	public void save(AddApi.Input input) throws StatusCodeWithException {
		ServiceMySqlModel model = serviceRepository.findOne("url", input.getUrl(), ServiceMySqlModel.class);
		if (model != null) {
			throw new StatusCodeWithException(StatusCode.PARAMETER_VALUE_INVALID, "url exists");
		}
		model = ModelMapper.map(input, ServiceMySqlModel.class);
		model.setCreatedBy(CurrentAccount.id());
		model.setCreatedTime(new Date());
		model.setUpdatedBy(CurrentAccount.id());
		model.setUpdatedTime(new Date());
		serviceRepository.save(model);
	}

	/**
	 * Paging query
	 */
	public PagingOutput<QueryApi.Output> query(QueryApi.Input input) {

		Where where = Where.create();
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
		if (StringUtils.isNotBlank(input.getConditionFields())) {
			model.setConditionFields(input.getConditionFields());
		}
		if (input.getServiceType() != -1) {
			model.setServiceType(input.getServiceType());
		}
		if (input.getStatus() != -1) {
			model.setStatus(input.getStatus());
		}
		serviceRepository.save(model);
	}

	public Output sqlTest(com.welab.wefe.serving.service.api.service.ServiceSQLTestApi.Input input)
			throws StatusCodeWithException {
//		String queryParams = input.getQueryParams();
		JObject params = JObject.create(input.getParams());
		JSONArray dataSource = JObject.parseArray(input.getDataSource());
		JSONArray conditionFields = JObject.parseArray(input.getConditionFields());
		String tableName = parseTableName(dataSource, 0);
		String dataSourceId = dataSource.getJSONObject(0).getString("id");
		String resultfields = parseReturnFields(dataSource, 0);
		String where = parseWhere(conditionFields, params);
		String sql = "SELECT " + resultfields + " FROM " + tableName + " WHERE " + where;
		System.out.println(sql);
		Map<String, Object> result = dataSourceService.execute(dataSourceId, sql,
				Arrays.asList(resultfields.split(",")));
		Output out = new Output();
		out.setResult(JObject.create(result));
		return out;
	}

	private String parseTableName(JSONArray dataSource, int index) {
		JSONObject json = dataSource.getJSONObject(index);
		return json.getString("db") + "." + json.getString("table");
	}

	private String parseReturnFields(JSONArray dataSource, int index) {
		JSONObject json = dataSource.getJSONObject(index);
		JSONArray returnFields = json.getJSONArray("return_fields");
		String resultFields = "";
		if (resultFields.isEmpty()) {
			resultFields = "*";
			return resultFields;
		} else {
			List<String> fields = new ArrayList<>();
			for (int i = 0; i < returnFields.size(); i++) {
				fields.add(returnFields.getJSONObject(i).getString("name"));
			}
			return StringUtils.join(fields, ",");
		}
	}

	private String parseWhere(JSONArray conditionFields, JObject params) {
		String where = "";
		if (conditionFields.isEmpty()) {
			where = "1=1";
			return where;
		} else {
			for (int i = 0; i < conditionFields.size(); i++) {
				JSONObject tmp = conditionFields.getJSONObject(i);
				where += (" " + tmp.getString("field_on_table") + "="
						+ params.getString(tmp.getString("field_on_param")) + " " + " " + tmp.getString("operator"));
			}
			return where;
		}
	}
}

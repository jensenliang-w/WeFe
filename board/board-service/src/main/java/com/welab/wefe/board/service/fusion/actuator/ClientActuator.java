package com.welab.wefe.board.service.fusion.actuator;

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


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.welab.wefe.board.service.api.fusion.actuator.psi.DownBloomFilterApi;
import com.welab.wefe.board.service.api.fusion.actuator.psi.PsiHandleApi;
import com.welab.wefe.board.service.exception.MemberGatewayException;
import com.welab.wefe.board.service.service.DataSetStorageService;
import com.welab.wefe.board.service.service.GatewayService;
import com.welab.wefe.board.service.service.fusion.FieldInfoService;
import com.welab.wefe.board.service.service.fusion.FusionResultStorageService;
import com.welab.wefe.board.service.util.primarykey.FieldInfo;
import com.welab.wefe.board.service.util.primarykey.PrimaryKeyUtils;
import com.welab.wefe.common.data.storage.common.Constant;
import com.welab.wefe.common.data.storage.model.DataItemModel;
import com.welab.wefe.common.data.storage.model.PageInputModel;
import com.welab.wefe.common.data.storage.model.PageOutputModel;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.JObject;
import com.welab.wefe.common.web.Launcher;
import com.welab.wefe.fusion.core.actuator.psi.PsiClientActuator;
import com.welab.wefe.fusion.core.dto.PsiActuatorMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hunter.zhao
 */
public class ClientActuator extends PsiClientActuator {
    public List<String> columnList;

    /**
     * Fragment size, default 10000
     */
    private int shard_size = 1000;
    private int current_index = 0;
    public List<FieldInfo> fieldInfoList;
    public String dstMemberId;
    DataSetStorageService dataSetStorageService;
    FusionResultStorageService fusionResultStorageService;

    private String[] headers;

    public ClientActuator(String businessId, String dataSetId, Boolean isTrace, String traceColumn, String dstMemberId) {
        super(businessId, dataSetId, isTrace, traceColumn);
        this.dstMemberId = dstMemberId;
    }

    @Override
    public void init() throws StatusCodeWithException {
        FieldInfoService service = Launcher.getBean(FieldInfoService.class);

        columnList = service.columnList(businessId);


        /**
         * Calculate the fragment size based on the number of fields
         */
        shard_size = shard_size / columnList.size();

        /**
         * Supplementary trace field
         */
        if (isTrace) {
            columnList.add(traceColumn);
        }

        /**
         * Find primary key composition fields
         */
        fieldInfoList = service.fieldInfoList(businessId);

        dataSetStorageService = Launcher.CONTEXT.getBean(DataSetStorageService.class);
        DataItemModel model = dataSetStorageService.getByKey(
                Constant.DBName.WEFE_DATA,
                dataSetStorageService.createRawDataSetTableName(dataSetId) + ".meta",
                "header"
        );
        headers = model.getV().toString().split(",");
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public List<JObject> next() {
        long start = System.currentTimeMillis();
        synchronized (dataSetStorageService) {

            PageOutputModel model = dataSetStorageService.getListByPage(
                    Constant.DBName.WEFE_DATA,
                    dataSetStorageService.createRawDataSetTableName(dataSetId),
                    new PageInputModel(current_index, shard_size)
            );

            List<DataItemModel> list = model.getData();

            List<JObject> curList = Lists.newArrayList();
            list.forEach(x -> {
                String[] values = x.getV().toString().split(",");
                JObject jObject = JObject.create();
                for (int i = 0; i < headers.length; i++) {
                    jObject.put(headers[i], values[i]);
                }
                curList.add(jObject);
            });


            LOG.info("cursor {} spend: {} curList {}", current_index, System.currentTimeMillis() - start, curList.size());

            current_index++;

            return curList;
        }
    }

    @Override
    public void dump(List<JObject> fruit) {
        LOG.info("fruit insert ready...");

        if (fruit.isEmpty()) {
            return;
        }

        LOG.info("fruit inserting...");


        //Build table
        //  createTable(businessId, new ArrayList<>(fruit.get(0).keySet()));
       // fusionResultStorageService.saveDataRow(businessId,fruit);
        /**
         * Fruit Standard formatting
         */
        List<Map<String, Object>> fruits = fruit.
                stream().
                map(new Function<JObject, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(JObject x) {
                        Map<String, Object> map = new LinkedHashMap();
                        for (Map.Entry<String, Object> column : x.entrySet()) {
                            map.put(column.getKey(), column.getValue());
                        }
                        return map;
                    }
                }).collect(Collectors.toList());

//        TaskResultManager.saveTaskResultRows(businessId, fruits);

        LOG.info("fruit insert end...");

        System.out.println("测试结果：" + JSON.toJSONString(fruit));
    }

    @Override
    public Boolean hasNext() {
        if (dataSetStorageService == null) {
            dataSetStorageService = Launcher.CONTEXT.getBean(DataSetStorageService.class);
        }

        synchronized (dataSetStorageService) {
            PageOutputModel model = dataSetStorageService.getListByPage(
                    Constant.DBName.WEFE_DATA,
                    dataSetStorageService.createRawDataSetTableName(dataSetId),
                    new PageInputModel(current_index, shard_size)
            );
            return model.getData().size() > 0;
        }
    }

    @Override
    public PsiActuatorMeta downloadBloomFilter() {

        LOG.info("downloadBloomFilter start");

        //调用gateway
        GatewayService gatewayService = Launcher.getBean(GatewayService.class);
        JSONObject result = null;
        try {
            result = gatewayService.callOtherMemberBoard(dstMemberId, DownBloomFilterApi.class, new DownBloomFilterApi.Input(businessId), JSONObject.class);
        } catch (MemberGatewayException e) {
            e.printStackTrace();
        }

        LOG.info("downloadBloomFilter end {} ", result);

        return JObject.toJavaObject(result, PsiActuatorMeta.class);
    }

    @Override
    public byte[][] qureyFusionData(byte[][] bs) {

        LOG.info("qureyFusionData start");

        //调用gateway
        GatewayService gatewayService = Launcher.getBean(GatewayService.class);
        List<byte[]> s = Lists.newArrayList();
        s.addAll(Arrays.asList(bs));
        JSONObject result = null;
        try {
            result = gatewayService.callOtherMemberBoard(dstMemberId, PsiHandleApi.class, new PsiHandleApi.Input(businessId, s), JSONObject.class);
        } catch (MemberGatewayException e) {
            e.printStackTrace();
        }

        LOG.info("qureyFusionData start");
        return (byte[][]) result.get("data");
    }

    @Override
    public void sendFusionData(List<byte[]> rs) {
    }

    @Override
    public String hashValue(JObject value) {
        return PrimaryKeyUtils.create(value, fieldInfoList);
    }
}

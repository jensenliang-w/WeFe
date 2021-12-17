
/*
 * *
 *  * Copyright 2021 Tianmian Tech. All Rights Reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.welab.wefe.mpc.pir.sdk.trasfer.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.welab.wefe.mpc.pir.PrivateInformationRetrievalApiName;
import com.welab.wefe.mpc.pir.request.*;
import com.welab.wefe.mpc.pir.sdk.config.PrivateInformationRetrievalConfig;
import com.welab.wefe.mpc.pir.sdk.trasfer.PrivateInformationRetrievalTransferVariable;
import com.welab.wefe.mpc.util.SignUtil;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class HttpTransferVariable implements PrivateInformationRetrievalTransferVariable {

    private PrivateInformationRetrievalConfig mConfig;

    public HttpTransferVariable(PrivateInformationRetrievalConfig config) {
        mConfig = config;
    }

    @Override
    public QueryRandomResponse queryRandom(QueryRandomRequest request) {
        return JSON.parseObject(query(mConfig.getServerUrl(), javaBeanToRequestJsonString(request, PrivateInformationRetrievalApiName.RANDOM)), QueryRandomResponse.class);
    }

    @Override
    public QueryRandomLegalResponse queryRandomLegal(QueryRandomLegalRequest request) {
        return JSON.parseObject(query(mConfig.getServerUrl(), javaBeanToRequestJsonString(request, PrivateInformationRetrievalApiName.RANDOM_LEGAL)), QueryRandomLegalResponse.class);
    }

    @Override
    public QueryKeysResponse queryKeys(QueryKeysRequest request) {
        return JSON.parseObject(query(mConfig.getServerUrl(), javaBeanToRequestJsonString(request, PrivateInformationRetrievalApiName.KEYS)), QueryKeysResponse.class);
    }

    @Override
    public QueryResultsResponse queryResults(QueryResultsRequest request) {
        return JSON.parseObject(query(mConfig.getServerUrl(), javaBeanToRequestJsonString(request, PrivateInformationRetrievalApiName.RESULTS)), QueryResultsResponse.class);
    }

    private JSONObject javaBeanToRequestJsonString(Object data, String apiName) {
        JSONObject jsonData = new JSONObject();
        if (data != null) {
            jsonData.put("jsonData", JSON.toJSONString(data));
        }
        jsonData.put("apiName", apiName);
        return jsonData;
    }

    private String query(String url, JSONObject params) {
        params = new JSONObject(new TreeMap(params));
        String data = params.toJSONString();
        if (mConfig.isNeedSign()) {
            String sign = SignUtil.sign(data, mConfig.getSignPrivateKey());
            JSONObject body = new JSONObject();
            body.put("member_id", mConfig.getCommercialId());
            body.put("sign", sign);
            body.put("data", data);
            data = body.toJSONString();
        }

        String response = HttpUtil.post(url, data);
        while (StrUtil.isEmpty(response)) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = HttpUtil.post(url, data);
        }

        return response;
    }
}

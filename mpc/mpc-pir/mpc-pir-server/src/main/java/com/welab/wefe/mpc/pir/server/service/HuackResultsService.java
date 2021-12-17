
/*
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

package com.welab.wefe.mpc.pir.server.service;

import com.alibaba.fastjson.JSON;
import com.welab.wefe.mpc.cache.CacheOperation;
import com.welab.wefe.mpc.commom.Constants;
import com.welab.wefe.mpc.pir.request.QueryResultsRequest;
import com.welab.wefe.mpc.pir.request.QueryResultsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author eval
 * @Date 2021/12/14
 **/
@Service
public class HuackResultsService {

    private static final Logger LOG = LoggerFactory.getLogger(HuackResultsService.class);
    @Autowired
    CacheOperation mCacheOperation;

    public QueryResultsResponse handle(QueryResultsRequest request) {
        // TODO
        long start = System.currentTimeMillis();
        QueryResultsResponse response = new QueryResultsResponse();
        String uuid = request.getUuid();
        String result = mCacheOperation.get(uuid, Constants.PIR.RESULT);
        while (result == null || result.isEmpty()) {
            result = mCacheOperation.get(uuid, Constants.PIR.RESULT);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        response.setUuid(uuid);
        response.setResults(JSON.parseArray(result, String.class));
        LOG.info("uuid:{} send result cost:{}",
                uuid, (System.currentTimeMillis() - start));
        return response;
    }
}

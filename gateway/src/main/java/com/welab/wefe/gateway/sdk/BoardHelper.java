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

package com.welab.wefe.gateway.sdk;

import com.welab.wefe.common.http.HttpRequest;
import com.welab.wefe.common.http.HttpResponse;
import com.welab.wefe.common.util.JObject;
import com.welab.wefe.common.util.RSAUtil;
import com.welab.wefe.common.util.StringUtil;
import com.welab.wefe.gateway.cache.MemberCache;
import com.welab.wefe.gateway.entity.MemberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Board service tool class
 *
 * @author aaron.li
 **/
public class BoardHelper {
    private static final Logger LOG = LoggerFactory.getLogger(BoardHelper.class);
    public static final String POST = "post";

    private final static String RESP_CODE_SUCCESS = "0";

    public static HttpResponse push(String url, String method, Map<String, String> headers, String body) {
        HttpRequest httpRequest = HttpRequest.create(url);
        httpRequest.setTimeout(60 * 1000);
        httpRequest.putHeaders(headers);
        httpRequest.setBody(body);
        return POST.equalsIgnoreCase(method) ? httpRequest.postJson() : httpRequest.get();
    }

    /**
     * Test the availability of board services
     */
    public static void testAvailable() throws Exception {
        MemberEntity selfMember = MemberCache.getInstance().getSelfMember();
        String boardBaseUrl = selfMember.getBoardUri();

        try {
            JObject reqBody = JObject.create().append("callerMemberId", selfMember.getId());
            reqBody.append("api", "gateway/test_route_connect").append("data", JObject.create());
            HttpResponse httpResponse = BoardHelper.push(boardBaseUrl + "gateway/redirect", BoardHelper.POST, new HashMap<>(16), generateReqParam(reqBody.toString()));
            if (!httpResponse.success()) {
                throw new Exception("board 响应失败，httpCode：" + httpResponse.getCode());
            }

            JObject responseBody = JObject.create(httpResponse.getBodyAsString());
            String code = responseBody.getString("code");
            if (!RESP_CODE_SUCCESS.equals(code)) {
                throw new Exception("board 响应失败，code：" + code + ", message: " + responseBody.getString("message"));
            }
        } catch (Exception e) {
            LOG.error("Test board service availability exception：", e);
            throw new Exception("board 响应失败：" + e.getMessage());
        }
    }


    /**
     * Generate request body
     */
    public static String generateReqParam(String body) throws Exception {
        return JObject.create()
                .append("member_id", MemberCache.getInstance().getSelfMember().getId())
                .append("data", StringUtil.isEmpty(body) ? "" : body)
                .append("sign", generateSign(body)).toJSONString();
    }

    /**
     * Generate signature
     *
     * @param body Message request body
     * @return signature
     */
    private static String generateSign(String body) throws Exception {
        String privateKey = MemberCache.getInstance().getSelfMember().getPrivateKey();

        return RSAUtil.sign(StringUtil.isEmpty(body) ? "" : body, privateKey, "UTF-8");
    }
}

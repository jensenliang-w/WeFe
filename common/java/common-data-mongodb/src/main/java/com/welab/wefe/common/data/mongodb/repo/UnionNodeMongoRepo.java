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

package com.welab.wefe.common.data.mongodb.repo;

import com.mongodb.client.result.UpdateResult;
import com.welab.wefe.common.data.mongodb.entity.union.DataSetDefaultTag;
import com.welab.wefe.common.data.mongodb.entity.union.UnionNode;
import com.welab.wefe.common.data.mongodb.entity.union.ext.DataSetDefaultTagExtJSON;
import com.welab.wefe.common.data.mongodb.entity.union.ext.UnionNodeExtJSON;
import com.welab.wefe.common.data.mongodb.util.QueryBuilder;
import com.welab.wefe.common.data.mongodb.util.UpdateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yuxin.zhang
 **/
@Repository
public class UnionNodeMongoRepo extends AbstractMongoRepo {
    public List<UnionNode> findAll() {
        return mongoTemplate.findAll(UnionNode.class);
    }

    public boolean deleteByUnionNodeId(String unionNodeId) {
        if (StringUtils.isEmpty(unionNodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("unionNodeId", unionNodeId).build();
        Update udpate = new UpdateBuilder().append("status", 1).build();
        UpdateResult updateResult = mongoTemplate.updateFirst(query, udpate, UnionNode.class);
        return updateResult.wasAcknowledged();
    }

    public boolean update(String unionNodeId,String sign,String unionBaseUrl,String organizationName,String updatedTime) {
        if (StringUtils.isEmpty(unionNodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("unionNodeId", unionNodeId).build();
        Update udpate = new UpdateBuilder()
                .append("sign", sign)
                .append("unionBaseUrl", unionBaseUrl)
                .append("organizationName", organizationName)
                .append("updatedTime", updatedTime)
                .build();
        UpdateResult updateResult = mongoTemplate.updateFirst(query, udpate, UnionNode.class);
        return updateResult.wasAcknowledged();
    }

    public boolean updateEnable(String unionNodeId,String enable,String updatedTime) {
        if (StringUtils.isEmpty(unionNodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("unionNodeId", unionNodeId).build();
        Update udpate = new UpdateBuilder()
                .append("enable", enable)
                .append("updatedTime", updatedTime)
                .build();
        UpdateResult updateResult = mongoTemplate.updateFirst(query, udpate, UnionNode.class);
        return updateResult.wasAcknowledged();
    }

    public boolean updateExtJSONById(String unionNodeId, UnionNodeExtJSON extJSON) {
        if (StringUtils.isEmpty(unionNodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("unionNodeId", unionNodeId).build();
        Update update = new UpdateBuilder().append("extJson", extJSON).build();
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, DataSetDefaultTag.class);
        return updateResult.wasAcknowledged();
    }
}

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
import com.welab.wefe.common.data.mongodb.entity.union.UnionNode;
import com.welab.wefe.common.data.mongodb.entity.union.ext.UnionNodeExtJSON;
import com.welab.wefe.common.data.mongodb.util.QueryBuilder;
import com.welab.wefe.common.data.mongodb.util.UpdateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yuxin.zhang
 **/
@Repository
public class UnionNodeMongoRepo extends AbstractMongoRepo {
    @Autowired
    protected MongoTemplate mongoUnionTemplate;

    @Override
    protected MongoTemplate getMongoTemplate() {
        return mongoUnionTemplate;
    }

    public List<UnionNode> findAll(boolean status) {
        return mongoUnionTemplate.find(
                new QueryBuilder()
                        .append("status", String.valueOf(status ? 1 : 0))
                        .notRemoved()
                        .build()
                ,
                UnionNode.class);
    }

    public List<UnionNode> findExcludeCurrentNode(String blockchainNodeId) {
        return mongoUnionTemplate.find(
                new QueryBuilder()
                        .notEq("blockchainNodeId", blockchainNodeId)
                        .notRemoved()
                        .build()
                ,
                UnionNode.class);
    }


    public UnionNode findByUnionBaseUrl(String unionBaseUrl) {
        return mongoUnionTemplate.findOne(
                new QueryBuilder()
                        .append("baseUrl", unionBaseUrl)
                        .notRemoved()
                        .build()
                ,
                UnionNode.class);
    }

    public UnionNode findByBlockchainNodeId(String blockchainNodeId) {
        return mongoUnionTemplate.findOne(
                new QueryBuilder()
                        .append("blockchainNodeId", blockchainNodeId)
                        .notRemoved()
                        .build()
                ,
                UnionNode.class);
    }

    public boolean deleteByUnionNodeId(String nodeId) {
        if (StringUtils.isEmpty(nodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("nodeId", nodeId).build();
        Update udpate = new UpdateBuilder().append("status", 1).build();
        UpdateResult updateResult = mongoUnionTemplate.updateFirst(query, udpate, UnionNode.class);
        return updateResult.wasAcknowledged();
    }

    public boolean update(
            String nodeId,
            String blockchainNodeId,
            String baseUrl,
            String organizationName,
            String contactEmail,
            String version,
            String updatedTime

    ) {
        if (StringUtils.isEmpty(nodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("nodeId", nodeId).build();
        Update udpate = new UpdateBuilder()
                .append("blockchainNodeId", blockchainNodeId)
                .append("baseUrl", baseUrl)
                .append("organizationName", organizationName)
                .append("contactEmail", contactEmail)
                .append("version", version)
                .append("updatedTime", updatedTime)
                .build();
        UpdateResult updateResult = mongoUnionTemplate.updateFirst(query, udpate, UnionNode.class);
        return updateResult.wasAcknowledged();
    }

    public boolean updateEnable(String nodeId, String enable, String updatedTime) {
        if (StringUtils.isEmpty(nodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("nodeId", nodeId).build();
        Update udpate = new UpdateBuilder()
                .append("enable", enable)
                .append("updatedTime", updatedTime)
                .build();
        UpdateResult updateResult = mongoUnionTemplate.updateFirst(query, udpate, UnionNode.class);
        return updateResult.wasAcknowledged();
    }

    public boolean updateExtJSONById(String nodeId, UnionNodeExtJSON extJSON) {
        if (StringUtils.isEmpty(nodeId)) {
            return false;
        }
        Query query = new QueryBuilder().append("nodeId", nodeId).build();
        Update update = new UpdateBuilder().append("extJson", extJSON).build();
        UpdateResult updateResult = mongoUnionTemplate.updateFirst(query, update, UnionNode.class);
        return updateResult.wasAcknowledged();
    }
}

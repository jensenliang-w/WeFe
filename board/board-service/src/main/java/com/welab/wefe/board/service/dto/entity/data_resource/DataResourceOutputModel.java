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
package com.welab.wefe.board.service.dto.entity.data_resource;

import com.alibaba.fastjson.JSONObject;
import com.welab.wefe.board.service.dto.entity.AbstractOutputModel;
import com.welab.wefe.common.enums.ComponentType;
import com.welab.wefe.common.enums.DataSetPublicLevel;
import com.welab.wefe.common.enums.DataSetStorageType;

/**
 * @author zane
 * @date 2021/12/1
 */
public class DataResourceOutputModel extends AbstractOutputModel {
    /**
     * 资源名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 标签
     */
    private String tags;
    /**
     * 存储类型
     */
    private DataSetStorageType storageType;
    /**
     * 资源在存储中的命名空间;库名、目录路径）
     */
    private String storageNamespace;
    /**
     * 资源在存储中的名称;表名、文件名）
     */
    private String storageResourceName;
    /**
     * 总数据量
     */
    private Long totalDataCount;
    /**
     * 资源的可见性
     */
    private DataSetPublicLevel publicLevel;
    /**
     * 可见成员列表;只有在列表中的联邦成员才可以看到该资源的基本信息
     */
    private String publicMemberList;
    /**
     * 该资源在多少个job中被使用
     */
    private Integer usageCountInJob;
    /**
     * 该资源在多少个flow中被使用
     */
    private Integer usageCountInFlow;
    /**
     * 该资源在多少个project中被使用
     */
    private Integer usageCountInProject;
    /**
     * 该资源被多少个其他成员被使用
     */
    private Integer usageCountInMember;
    /**
     * 是否是衍生资源
     */
    private boolean derivedResource;
    /**
     * 衍生来源，枚举;原始、对齐、分箱）
     */
    private ComponentType derivedFrom;
    /**
     * 衍生来源流程id
     */
    private String derivedFromFlowId;
    /**
     * 衍生来源任务id
     */
    private String derivedFromJobId;
    /**
     * 衍生来源子任务id
     */
    private String derivedFromTaskId;
    /**
     * 该数据资源相关的统计信息
     */
    private JSONObject statisticalInformation;

    // region getter/setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public DataSetStorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(DataSetStorageType storageType) {
        this.storageType = storageType;
    }

    public String getStorageNamespace() {
        return storageNamespace;
    }

    public void setStorageNamespace(String storageNamespace) {
        this.storageNamespace = storageNamespace;
    }

    public String getStorageResourceName() {
        return storageResourceName;
    }

    public void setStorageResourceName(String storageResourceName) {
        this.storageResourceName = storageResourceName;
    }

    public Long getTotalDataCount() {
        return totalDataCount;
    }

    public void setTotalDataCount(Long totalDataCount) {
        this.totalDataCount = totalDataCount;
    }

    public DataSetPublicLevel getPublicLevel() {
        return publicLevel;
    }

    public void setPublicLevel(DataSetPublicLevel publicLevel) {
        this.publicLevel = publicLevel;
    }

    public String getPublicMemberList() {
        return publicMemberList;
    }

    public void setPublicMemberList(String publicMemberList) {
        this.publicMemberList = publicMemberList;
    }

    public Integer getUsageCountInJob() {
        return usageCountInJob;
    }

    public void setUsageCountInJob(Integer usageCountInJob) {
        this.usageCountInJob = usageCountInJob;
    }

    public Integer getUsageCountInFlow() {
        return usageCountInFlow;
    }

    public void setUsageCountInFlow(Integer usageCountInFlow) {
        this.usageCountInFlow = usageCountInFlow;
    }

    public Integer getUsageCountInProject() {
        return usageCountInProject;
    }

    public void setUsageCountInProject(Integer usageCountInProject) {
        this.usageCountInProject = usageCountInProject;
    }

    public Integer getUsageCountInMember() {
        return usageCountInMember;
    }

    public void setUsageCountInMember(Integer usageCountInMember) {
        this.usageCountInMember = usageCountInMember;
    }

    public boolean isDerivedResource() {
        return derivedResource;
    }

    public void setDerivedResource(boolean derivedResource) {
        this.derivedResource = derivedResource;
    }

    public ComponentType getDerivedFrom() {
        return derivedFrom;
    }

    public void setDerivedFrom(ComponentType derivedFrom) {
        this.derivedFrom = derivedFrom;
    }

    public String getDerivedFromFlowId() {
        return derivedFromFlowId;
    }

    public void setDerivedFromFlowId(String derivedFromFlowId) {
        this.derivedFromFlowId = derivedFromFlowId;
    }

    public String getDerivedFromJobId() {
        return derivedFromJobId;
    }

    public void setDerivedFromJobId(String derivedFromJobId) {
        this.derivedFromJobId = derivedFromJobId;
    }

    public String getDerivedFromTaskId() {
        return derivedFromTaskId;
    }

    public void setDerivedFromTaskId(String derivedFromTaskId) {
        this.derivedFromTaskId = derivedFromTaskId;
    }

    public JSONObject getStatisticalInformation() {
        return statisticalInformation;
    }

    public void setStatisticalInformation(JSONObject statisticalInformation) {
        this.statisticalInformation = statisticalInformation;
    }


    // endregion
}

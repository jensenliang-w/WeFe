/**
 * Copyright 2021 The WeFe Authors. All Rights Reserved.
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

package com.welab.wefe.data.fusion.service.database.entity;

import com.welab.wefe.common.enums.ColumnDataType;
import com.welab.wefe.common.web.dto.AbstractApiOutput;

import java.util.Map;

/**
 * @author Zane
 * @date 2020/5/21
 */
public class DataSetColumnOutputModel extends AbstractApiOutput {

    /**
     * Data set Id
     */
    private String dataSetId;
    /**
     * The serial number field
     */
    private Integer index;
    /**
     * The field names
     */
    private String name;
    /**
     * The data type
     */
    private ColumnDataType dataType;
    /**
     * comment
     */
    private String comment;
    /**
     * Number of null data rows
     */
    private Long emptyRows;
    /**
     * Numerical distribution
     */
    private Map<String, Object> valueDistribution;


    //region getter/setter

    public String getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(String dataSetId) {
        this.dataSetId = dataSetId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnDataType getDataType() {
        return dataType;
    }

    public void setDataType(ColumnDataType dataType) {
        this.dataType = dataType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getEmptyRows() {
        return emptyRows;
    }

    public void setEmptyRows(Long emptyRows) {
        this.emptyRows = emptyRows;
    }

    public Map<String, Object> getValueDistribution() {
        return valueDistribution;
    }

    public void setValueDistribution(Map<String, Object> valueDistribution) {
        this.valueDistribution = valueDistribution;
    }


    //endregion

}

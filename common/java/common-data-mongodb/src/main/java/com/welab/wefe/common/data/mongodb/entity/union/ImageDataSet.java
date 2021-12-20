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

package com.welab.wefe.common.data.mongodb.entity.union;

import com.welab.wefe.common.data.mongodb.constant.MongodbTable;
import com.welab.wefe.common.data.mongodb.entity.base.AbstractBlockChainBusinessModel;
import com.welab.wefe.common.data.mongodb.entity.union.ext.ImageDataSetExtJSON;
import com.welab.wefe.common.enums.DeepLearningJobType;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author yuxin.zhang
 **/
@Document(collection = MongodbTable.Union.IMAGE_DATASET)
public class ImageDataSet extends AbstractBlockChainBusinessModel {
    private String dataResourceId;
    private DeepLearningJobType forJobType;
    private String labelList;
    private String labeledCount;
    private String labelCompleted;
    private String fileSize;
    private ImageDataSetExtJSON extJson;

    public String getDataResourceId() {
        return dataResourceId;
    }

    public void setDataResourceId(String dataResourceId) {
        this.dataResourceId = dataResourceId;
    }

    public DeepLearningJobType getForJobType() {
        return forJobType;
    }

    public void setForJobType(DeepLearningJobType forJobType) {
        this.forJobType = forJobType;
    }

    public String getLabelList() {
        return labelList;
    }

    public void setLabelList(String labelList) {
        this.labelList = labelList;
    }

    public String getLabeledCount() {
        return labeledCount;
    }

    public void setLabeledCount(String labeledCount) {
        this.labeledCount = labeledCount;
    }

    public String getLabelCompleted() {
        return labelCompleted;
    }

    public void setLabelCompleted(String labelCompleted) {
        this.labelCompleted = labelCompleted;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public ImageDataSetExtJSON getExtJson() {
        return extJson;
    }

    public void setExtJson(ImageDataSetExtJSON extJson) {
        this.extJson = extJson;
    }
}

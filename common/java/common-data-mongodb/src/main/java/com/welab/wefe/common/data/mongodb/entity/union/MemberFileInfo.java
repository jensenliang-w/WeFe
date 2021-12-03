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
import com.welab.wefe.common.data.mongodb.entity.union.ext.MemberFileInfoExtJSON;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author yuxin.zhang
 **/
@Document(collection = MongodbTable.Union.MEMBER_FILE_INFO)
public class MemberFileInfo extends AbstractBlockChainBusinessModel {
    private String fileId;
    private String fileSign;
    private String fileName;
    private String memberId;
    private String reporter;
    private String purpose;
    private String enable;
    private MemberFileInfoExtJSON extJson;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileSign() {
        return fileSign;
    }

    public void setFileSign(String fileSign) {
        this.fileSign = fileSign;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public MemberFileInfoExtJSON getExtJson() {
        return extJson;
    }

    public void setExtJson(MemberFileInfoExtJSON extJson) {
        this.extJson = extJson;
    }
}

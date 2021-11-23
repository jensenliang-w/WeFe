package com.welab.wefe.common.data.mongodb.entity.union.ext;

import javax.persistence.Column;
import java.util.List;

/**
 * @Description:
 * @author: yuxin.zhang
 * @date: 2021/10/19
 */
public class MemberExtJSON {
    private int realNameAuthStatus;
    private String principalName;
    private String authType;
    private String auditComment;
    private String description;
    private List<RealNameAuthFileInfo> realNameAuthFileInfoList;


    public int getRealNameAuthStatus() {
        return realNameAuthStatus;
    }

    public void setRealNameAuthStatus(int realNameAuthStatus) {
        this.realNameAuthStatus = realNameAuthStatus;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RealNameAuthFileInfo> getRealNameAuthFileInfoList() {
        return realNameAuthFileInfoList;
    }

    public void setRealNameAuthFileInfoList(List<RealNameAuthFileInfo> realNameAuthFileInfoList) {
        this.realNameAuthFileInfoList = realNameAuthFileInfoList;
    }

    public String getAuditComment() {
        return auditComment;
    }

    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
}

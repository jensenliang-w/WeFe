package com.welab.wefe.common.data.mongodb.entity.union.ext;

import java.util.List;

/**
 * @Description:
 * @author: yuxin.zhang
 * @date: 2021/10/19
 */
public class MemberExtJSON {
    private int realNameAuthStatus;
    private String reporter;
    private String principalName;
    private String authType;
    private String auditComment;
    private String description;
    private List<RealnameAuthFileInfo> realnameAuthFileInfoList;


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


    public String getAuditComment() {
        return auditComment;
    }

    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }


    public List<RealnameAuthFileInfo> getRealnameAuthFileInfoList() {
        return realnameAuthFileInfoList;
    }

    public void setRealnameAuthFileInfoList(List<RealnameAuthFileInfo> realnameAuthFileInfoList) {
        this.realnameAuthFileInfoList = realnameAuthFileInfoList;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
}

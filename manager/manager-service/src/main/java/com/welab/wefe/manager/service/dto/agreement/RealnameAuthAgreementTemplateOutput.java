package com.welab.wefe.manager.service.dto.agreement;

public class RealnameAuthAgreementTemplateOutput {
    private String templateFileId;
    private String templateFileSign;
    private String fileName;
    private int enable;
    private int status;

    public String getTemplateFileId() {
        return templateFileId;
    }

    public void setTemplateFileId(String templateFileId) {
        this.templateFileId = templateFileId;
    }

    public String getTemplateFileSign() {
        return templateFileSign;
    }

    public void setTemplateFileSign(String templateFileSign) {
        this.templateFileSign = templateFileSign;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

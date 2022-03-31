/*
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welab.wefe.data.fusion.service.config;

import com.welab.wefe.common.wefe.enums.env.EnvBranch;
import com.welab.wefe.common.wefe.enums.env.EnvName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * config.properties
 *
 * @author lonnie
 */
@Component
@PropertySource(value = {"application.properties"}, encoding = "utf-8")
@ConfigurationProperties
public class Config {

    @Value("${wefe.job.work_mode}")
    private Integer workMode;

    @Value("${env.name}")
    private EnvName envName;

    /**
     * The branch of the environment, different branches will have different functions.
     * <p>
     * online_demo: You can only delete data created by yourself（eg:flow、member、data_set）
     */
    @Value("${env.branch:master}")
    private EnvBranch envBranch;

    @Value("${fc.access_key_id:xxx}")
    private String fcAccessKeyId;

    @Value("${fc.access_key_secret:xxx}")
    private String fcAccessKeySecret;

    @Value("${verification.code.send.channel:sms}")
    private String verificationCodeSendChannel;

    @Value("${sms.aliyun.sign.name:xxx}")
    private String smsAliyunSignName;

    @Value("${sms.aliyun.account.forget.password.verification.code.template.code:xxx}")
    private String smsAliyunAccountForgetPasswordVerificationCodeTemplateCode;

    @Value("${sms.aliyun.member.register.verification.code.template.code:xxx}")
    private String smsAliyunMemberregisterVerificationCodeTemplateCode;

    @Value("${email.account.forget.password.subject:忘记密码}")
    private String emailAccountForgetPasswordSubject;

    @Value("${email.account.forget.password.content:您正在执行忘记密码操作。您的验证码是#code#，2分钟内有效，请勿泄漏于他人!}")
    private String emailAccountForgetPasswordContent;

    @Value("${sm4.secret.key}")
    private String sm4SecretKey;

    @Value("${encrypt.phone.number.open:true}")
    private boolean encryptPhoneNumberOpen;

    public boolean isOnlineDemo() {
        return envBranch == EnvBranch.online_demo;
    }

    // region getter/setter

    public Integer getWorkMode() {
        return workMode;
    }

    public void setWorkMode(Integer workMode) {
        this.workMode = workMode;
    }

    public EnvName getEnvName() {
        return envName;
    }

    public void setEnvName(EnvName envName) {
        this.envName = envName;
    }

    public EnvBranch getEnvBranch() {
        return envBranch;
    }

    public void setEnvBranch(EnvBranch envBranch) {
        this.envBranch = envBranch;
    }

    public String getVerificationCodeSendChannel() {
        return verificationCodeSendChannel;
    }

    public void setVerificationCodeSendChannel(String verificationCodeSendChannel) {
        this.verificationCodeSendChannel = verificationCodeSendChannel;
    }

    public String getSmsAliyunAccountForgetPasswordVerificationCodeTemplateCode() {
        return smsAliyunAccountForgetPasswordVerificationCodeTemplateCode;
    }

    public void setSmsAliyunAccountForgetPasswordVerificationCodeTemplateCode(String smsAliyunAccountForgetPasswordVerificationCodeTemplateCode) {
        this.smsAliyunAccountForgetPasswordVerificationCodeTemplateCode = smsAliyunAccountForgetPasswordVerificationCodeTemplateCode;
    }

    public String getSmsAliyunMemberregisterVerificationCodeTemplateCode() {
        return smsAliyunMemberregisterVerificationCodeTemplateCode;
    }

    public void setSmsAliyunMemberregisterVerificationCodeTemplateCode(String smsAliyunMemberregisterVerificationCodeTemplateCode) {
        this.smsAliyunMemberregisterVerificationCodeTemplateCode = smsAliyunMemberregisterVerificationCodeTemplateCode;
    }

    public String getSmsAliyunSignName() {
        return smsAliyunSignName;
    }

    public void setSmsAliyunSignName(String smsAliyunSignName) {
        this.smsAliyunSignName = smsAliyunSignName;
    }

    public String getFcAccessKeyId() {
        return fcAccessKeyId;
    }

    public void setFcAccessKeyId(String fcAccessKeyId) {
        this.fcAccessKeyId = fcAccessKeyId;
    }

    public String getFcAccessKeySecret() {
        return fcAccessKeySecret;
    }

    public void setFcAccessKeySecret(String fcAccessKeySecret) {
        this.fcAccessKeySecret = fcAccessKeySecret;
    }

    public String getSm4SecretKey() {
        return sm4SecretKey;
    }

    public void setSm4SecretKey(String sm4SecretKey) {
        this.sm4SecretKey = sm4SecretKey;
    }

    public boolean isEncryptPhoneNumberOpen() {
        return encryptPhoneNumberOpen;
    }

    public void setEncryptPhoneNumberOpen(boolean encryptPhoneNumberOpen) {
        this.encryptPhoneNumberOpen = encryptPhoneNumberOpen;
    }

    // endregion

}

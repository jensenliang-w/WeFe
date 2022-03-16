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

package com.welab.wefe.serving.service.service;


import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.data.mysql.Where;
import com.welab.wefe.common.data.mysql.enums.OrderBy;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.Md5;
import com.welab.wefe.common.util.Sha1;
import com.welab.wefe.common.util.StringUtil;
import com.welab.wefe.common.web.CurrentAccount;
import com.welab.wefe.common.web.service.CaptchaService;
import com.welab.wefe.common.web.service.account.AbstractAccountService;
import com.welab.wefe.common.web.service.account.AccountInfo;
import com.welab.wefe.common.wefe.enums.AuditStatus;
import com.welab.wefe.serving.service.api.account.*;
import com.welab.wefe.serving.service.api.account.QueryAllApi.Output;
import com.welab.wefe.serving.service.database.serving.entity.AccountMySqlModel;
import com.welab.wefe.serving.service.database.serving.repository.AccountRepository;
import com.welab.wefe.serving.service.dto.PagingOutput;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Zane
 */
@Service
public class AccountService extends AbstractAccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Paging query
     */
//    public PagingOutput<AccountOutputModel> query(QueryApi.Input input) {
//
//        Specification<AccountMySqlModel> queryCondition = (Specification<AccountMySqlModel>) (root, query, cb) -> {
//            List<Predicate> list = new ArrayList<>();
//
//            if (StringUtil.isNotEmpty(input.getPhoneNumber())) {
//                list.add(cb.equal(root.getProcessor("phoneNumber"), input.getPhoneNumber()));
//            }
//
//            if (StringUtil.isNotEmpty(input.getNickname())) {
//                list.add(cb.like(root.getProcessor("nickname"), "%" + input.getNickname() + "%"));
//            }
//
//            return cb.and(list.toArray(new Predicate[list.size()]));
//        };
//        Pageable pageable = PageRequest
//                .of(
//                        input.getPageIndex(),
//                        input.getPageSize(),
//                        Sort.by(Sort.Direction.DESC, "createdTime")
//                );
//
//        Page<AccountMySqlModel> page = accountRepository.findAll(queryCondition, pageable);
//
//        return PagingOutput.of(
//                page.getTotalElements(),
//                page.getContent(),
//                AccountOutputModel.class
//        );
//    }

    /**
     * register
     */
    public String register(RegisterApi.Input input) throws StatusCodeWithException {
        //Verification code verification
        if (!CaptchaService.verify(input.getKey(), input.getCode())) {
            throw new StatusCodeWithException("验证码错误", StatusCode.PARAMETER_VALUE_INVALID);
        }

        //Judge whether the account has been registered
        AccountMySqlModel one = accountRepository.findOne("phoneNumber", input.getPhoneNumber(), AccountMySqlModel.class);
        if (one != null) {
            throw new StatusCodeWithException("该手机号已注册", StatusCode.DATA_EXISTED);
        }

        String salt = createRandomSalt();

        String password = Sha1.of(input.getPassword() + salt);

        AccountMySqlModel model = new AccountMySqlModel();
        model.setCreatedBy(CurrentAccount.id());
        model.setPhoneNumber(input.getPhoneNumber());
        model.setNickname(input.getNickname());
        model.setEmail(input.getEmail());
        model.setSalt(salt);
        model.setPassword(password);
        model.setSuperAdminRole(accountRepository.count() < 1);
        model.setAdminRole(model.getSuperAdminRole());


        // Super administrator does not need to review
        if (model.getSuperAdminRole()) {
            model.setAuditStatus(AuditStatus.agree);
            model.setAuditComment("超级管理员自动通过");
            model.setEnable(true);
        }
        // Whether others want to review it depends on the configuration.
        else {
            model.setAuditStatus(AuditStatus.auditing);
            model.setEnable(false);
        }
        accountRepository.save(model);

        CacheObjects.refreshAccountMap();

        return model.getId();
    }

    @Override
    public AccountInfo getAccountInfo(String phoneNumber) {
        AccountMySqlModel model = accountRepository.findByPhoneNumber(phoneNumber);
        return toAccountInfo(model);
    }

    @Override
    public AccountInfo getSuperAdmin() {
        List<AccountMySqlModel> list = accountRepository
                .findAll(Where.create().equal("superAdminRole", true).build(AccountMySqlModel.class));

        if (list.isEmpty()) {
            return null;
        }

        return toAccountInfo(list.get(0));
    }

    private AccountInfo toAccountInfo(AccountMySqlModel model) {
        if (model == null) {
            return null;
        }

        AccountInfo info = new AccountInfo();
        info.setId(model.getId());
        info.setPhoneNumber(model.getPhoneNumber());
        info.setNickname(model.getNickname());
        info.setPassword(model.getPassword());
        info.setSalt(model.getSalt());
        info.setAuditStatus(model.getAuditStatus());
        info.setAuditComment(model.getAuditComment());
        info.setAdminRole(model.getAdminRole());
        info.setSuperAdminRole(model.getSuperAdminRole());
        info.setEnable(model.getEnable());
        info.setCancelled(model.isCancelled());
        return info;
    }

    public List<Output> queryAll() {
        List<AccountMySqlModel> accounts = accountRepository.findAll();
        return accounts.stream().map(x -> com.welab.wefe.common.web.util.ModelMapper.map(x, Output.class))
                .collect(Collectors.toList());
    }

    public List<Output> query() {
        List<AccountMySqlModel> accounts = accountRepository.findAll();
        return accounts.stream().map(x -> com.welab.wefe.common.web.util.ModelMapper.map(x, Output.class))
                .collect(Collectors.toList());
    }

    /**
     * Paging query account
     */
    public PagingOutput<QueryApi.Output> query(QueryApi.Input input) throws StatusCodeWithException {

        Specification<AccountMySqlModel> where = Where.create().contains("phoneNumber", input.getPhoneNumber())
                .equal("auditStatus", input.getAuditStatus()).contains("nickname", input.getNickname())
                .orderBy("createdTime", OrderBy.desc).build(AccountMySqlModel.class);

        return accountRepository.paging(where, input, QueryApi.Output.class);
    }

    public void audit(AuditApi.Input input) throws StatusCodeWithException {
        AccountMySqlModel auditor = accountRepository.findById(CurrentAccount.id()).orElse(null);
        if (!auditor.getAdminRole()) {
            throw new StatusCodeWithException("您不是管理员，无权执行审核操作！", StatusCode.PARAMETER_VALUE_INVALID);
        }

        AccountMySqlModel account = accountRepository.findById(input.getAccountId()).orElse(null);
        if (account.getAuditStatus() != AuditStatus.auditing) {
            throw new StatusCodeWithException("该账号已被审核，请勿重复操作！", StatusCode.PARAMETER_VALUE_INVALID);
        }
        account.setEnable(true);
        account.setAuditStatus(input.getAuditStatus());
        account.setAuditComment(input.getAuditComment());
        account.setUpdatedBy(CurrentAccount.id());
        accountRepository.save(account);

    }

    /**
     * Update the user's enable status
     */
    public void enable(EnableApi.Input input) throws StatusCodeWithException {

        if (!CurrentAccount.isAdmin() && !CurrentAccount.isSuperAdmin()) {
            throw new StatusCodeWithException("普通账号无法进行此操作。", StatusCode.PERMISSION_DENIED);
        }

        if (input.getId().equals(CurrentAccount.id())) {
            throw new StatusCodeWithException("无法对自己进行此操作。", StatusCode.PERMISSION_DENIED);
        }

        AccountMySqlModel account = accountRepository.findById(input.getId()).orElse(null);
        if (account == null) {
            throw new StatusCodeWithException("找不到更新的账号信息。", StatusCode.DATA_NOT_FOUND);
        }

        if (account.getAdminRole() && !CurrentAccount.isSuperAdmin()) {
            throw new StatusCodeWithException("非超级管理员无法进行此操作。", StatusCode.PERMISSION_DENIED);
        }

        account.setEnable(input.getEnable());
        account.setUpdatedBy(CurrentAccount.id());
        account.setUpdatedTime(new Date());
        account.setAuditComment(input.getEnable() ? "管理员启用了该账号" : "管理员禁用了该账号");

        accountRepository.save(account);

        CurrentAccount.logout(input.getId());
    }

    /**
     * Update user basic information
     */
    public void update(UpdateApi.Input input) throws StatusCodeWithException {

        AccountMySqlModel account = accountRepository.findById(input.getId()).orElse(null);

        if (account == null) {
            throw new StatusCodeWithException("找不到更新的账号信息。", StatusCode.DATA_NOT_FOUND);
        }

        if (StringUtil.isNotEmpty(input.getNickname())) {
            account.setNickname(input.getNickname());
        }

        if (StringUtil.isNotEmpty(input.getEmail())) {
            account.setEmail(input.getEmail());
        }

        // Set someone else to be an administrator
        if (input.getAdminRole() != null) {
            if (!CurrentAccount.isSuperAdmin()) {
                throw new StatusCodeWithException("非超级管理员无法进行此操作。", StatusCode.PERMISSION_DENIED);
            }
            account.setAdminRole(input.getAdminRole());
        }

        account.setUpdatedBy(CurrentAccount.id());
        account.setUpdatedTime(new Date());

        accountRepository.save(account);
    }

    /**
     * Transfer the super administrator status to another account
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeSuperAdmin(AccountMySqlModel account) throws StatusCodeWithException {
        account.setAdminRole(true);
        account.setSuperAdminRole(true);
        account.setUpdatedBy(CurrentAccount.id());
        account.setUpdatedTime(new Date());

        // Update designated user as super administrator
        accountRepository.save(account);
        // Cancel the super administrator privileges of the current account
        accountRepository.cancelSuperAdmin(CurrentAccount.id());
    }

    /**
     * Reset user password (administrator rights)
     */
    public String resetPassword(ResetPasswordApi.Input input) throws StatusCodeWithException {
        if (!CurrentAccount.isAdmin()) {
            throw new StatusCodeWithException("非管理员无法重置密码。", StatusCode.PERMISSION_DENIED);
        }

        String phoneNumber = CurrentAccount.phoneNumber();
        if (phoneNumber == null) {
            throw new StatusCodeWithException(StatusCode.LOGIN_REQUIRED);
        }
        AccountMySqlModel currentAdmin = accountRepository.findByPhoneNumber(phoneNumber);
        // Check password
        if (!StringUtil.equals(currentAdmin.getPassword(), Sha1.of(input.getPassword() + currentAdmin.getSalt()))) {
            throw new StatusCodeWithException("密码不正确，请重新输入", StatusCode.PARAMETER_VALUE_INVALID);
        }

        AccountMySqlModel model = accountRepository.findById(input.getId()).orElse(null);
        if (model == null) {
            throw new StatusCodeWithException("找不到更新的账号信息。", StatusCode.DATA_NOT_FOUND);
        }

        if (!CurrentAccount.isAdmin()) {
            throw new StatusCodeWithException("非管理员无法重置密码。", StatusCode.PERMISSION_DENIED);
        }

        if (model.getSuperAdminRole()) {
            throw new StatusCodeWithException("不能重置超级管理员密码。", StatusCode.PERMISSION_DENIED);
        }

        String salt = createRandomSalt();
        String newPassword = RandomStringUtils.randomAlphanumeric(2) + new Random().nextInt(999999);

        String websitePassword = model.getPhoneNumber() + newPassword + model.getPhoneNumber() + model.getPhoneNumber().substring(0, 3) + newPassword.substring(newPassword.length() - 3);

        model.setSalt(salt);
        model.setPassword(Sha1.of(Md5.of(websitePassword) + salt));
        model.setUpdatedBy(CurrentAccount.id());
        model.setUpdatedTime(new Date());
        accountRepository.save(model);

        CurrentAccount.logout(model.getId());

        return newPassword;
    }

    /**
     * update password
     */
    public void updatePassword(String oldPassword, String newPassword) throws StatusCodeWithException {
        String phoneNumber = CurrentAccount.phoneNumber();
        if (phoneNumber == null) {
            throw new StatusCodeWithException(StatusCode.LOGIN_REQUIRED);
        }
        AccountMySqlModel model = accountRepository.findByPhoneNumber(phoneNumber);
        // Check old password
        if (!StringUtil.equals(model.getPassword(), Sha1.of(oldPassword + model.getSalt()))) {
            CurrentAccount.logout();
            throw new StatusCodeWithException("您输入的旧密码不正确，为确保安全，请重新登录后重试。", StatusCode.PARAMETER_VALUE_INVALID);
        }
        // Regenerate salt
        String salt = createRandomSalt();
        // sha hash
        newPassword = Sha1.of(newPassword + salt);
        model.setSalt(salt);
        model.setPassword(newPassword);
        accountRepository.save(model);
        CurrentAccount.logout(model.getId());
    }
}

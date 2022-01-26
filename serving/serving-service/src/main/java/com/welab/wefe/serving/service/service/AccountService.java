/**
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
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

package com.welab.wefe.serving.service.service;


import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.Base64Util;
import com.welab.wefe.common.util.Sha1;
import com.welab.wefe.common.web.CurrentAccount;
import com.welab.wefe.common.web.LoginSecurityPolicy;
import com.welab.wefe.common.web.service.CaptchaService;
import com.welab.wefe.serving.service.api.account.LoginApi;
import com.welab.wefe.serving.service.api.account.QueryApi.Output;
import com.welab.wefe.serving.service.api.account.RegisterApi;
import com.welab.wefe.serving.service.database.serving.entity.AccountMySqlModel;
import com.welab.wefe.serving.service.database.serving.repository.AccountRepository;

/**
 * @author Zane
 */
@Service
public class AccountService {

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
            throw new StatusCodeWithException("Verification code error！", StatusCode.PARAMETER_VALUE_INVALID);
        }

        //Judge whether the account has been registered
        AccountMySqlModel one = accountRepository.findOne("phoneNumber", input.getPhoneNumber(), AccountMySqlModel.class);
        if (one != null) {
            throw new StatusCodeWithException("The phone number has been registered！", StatusCode.DATA_EXISTED);
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
        accountRepository.save(model);

        CacheObjects.refreshAccountMap();

        return model.getId();
    }

    /**
     * login
     */
    public LoginApi.Output login(String phoneNumber, String password, String key, String code) throws StatusCodeWithException {
        //Verification code verification
//        if (!CaptchaService.verify(key, code)) {
//            throw new StatusCodeWithException("Verification code error！", StatusCode.PARAMETER_VALUE_INVALID);
//        }

        // Check whether it is in the small black room
        if (LoginSecurityPolicy.inDarkRoom(phoneNumber)) {
            throw new StatusCodeWithException("The account has been forbidden to log in. Please try again in an hour or contact the administrator.", StatusCode.PARAMETER_VALUE_INVALID);
        }

        AccountMySqlModel model = accountRepository.findOne("phoneNumber", phoneNumber, AccountMySqlModel.class);
        if (model == null || !model.getPassword().equals(Sha1.of(password + model.getSalt()))) {
            // Log a login failure event
            LoginSecurityPolicy.onLoginFail(phoneNumber);
            throw new StatusCodeWithException("Wrong mobile phone number or password！", StatusCode.PARAMETER_VALUE_INVALID);
        }

        String token = UUID.randomUUID().toString();

        LoginApi.Output output = new ModelMapper().map(model, LoginApi.Output.class);
        output.setToken(token);

        CurrentAccount.logined(token, model.getId(), model.getPhoneNumber());

        // Record a login success event
        LoginSecurityPolicy.onLoginSuccess(phoneNumber);

        return output;
    }

    /**
     * create salt
     */
    private String createRandomSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[16];
        r.nextBytes(salt);

        return Base64Util.encode(salt);
    }

	public List<Output> query() {
		List<AccountMySqlModel> accounts = accountRepository.findAll();
		return accounts.stream().map(x -> com.welab.wefe.serving.service.utils.ModelMapper.map(x, Output.class))
				.collect(Collectors.toList());
	}
}

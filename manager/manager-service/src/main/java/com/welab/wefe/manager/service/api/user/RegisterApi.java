package com.welab.wefe.manager.service.api.user;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiOutput;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.manager.service.dto.user.RegisterInput;
import com.welab.wefe.manager.service.dto.user.UserUpdateInput;
import com.welab.wefe.manager.service.mapper.UserMapper;
import com.welab.wefe.manager.service.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @Description:
 * @author: yuxin.zhang
 * @date: 2021/11/2
 */
@Api(path = "user/register", name = "register", login = false)
public class RegisterApi extends AbstractApi<RegisterInput, AbstractApiOutput> {
    @Autowired
    private UserService userService;

    private UserMapper mUserMapper = Mappers.getMapper(UserMapper.class);

    @Override
    protected ApiResult<AbstractApiOutput> handle(RegisterInput input) throws StatusCodeWithException, IOException {
        userService.register(mUserMapper.transfer(input));
        return success();
    }
}

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

package com.welab.wefe.serving.service.api.account;

import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.common.web.dto.NoneApiOutput;
import com.welab.wefe.serving.service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hunter.zhao
 */
@Api(path = "account/update_password", name = "Change Password")
public class UpdatePasswordApi extends AbstractApi<UpdatePasswordApi.Input, NoneApiOutput> {
    @Autowired
    private AccountService accountService;

    @Override
    protected ApiResult<NoneApiOutput> handle(Input input) {

        return success();
    }

    public static class Input extends AbstractApiInput {

        @Check(require = true)
        private String oldPassword;

        @Check(require = true)
        private String newPassword;

        //region getter/setter

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }


        //endregion
    }
}

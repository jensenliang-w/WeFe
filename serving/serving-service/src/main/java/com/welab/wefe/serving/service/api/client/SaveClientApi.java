package com.welab.wefe.serving.service.api.client;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractNoneOutputApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.serving.service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;


@Api(path = "client/save", name = "save")
public class SaveClientApi extends AbstractNoneOutputApi<SaveClientApi.Input> {


    @Autowired
    private ClientService clientService;

    @Override
    protected ApiResult<?> handler(Input input) throws StatusCodeWithException {

        clientService.save(input);
        return success();
    }


    public static class Input extends AbstractApiInput {

        @Check(name = "客户id")
        private String id;

        @Check(name = "客户名称", require = true)
        private String name;

        @Check(name = "客户邮箱")
        private String email;

        @Check(name = "客户调用端 IP 地址", require = true)
        private String ipAdd;

        @Check(name = "客户公钥", require = true)
        private String pubKey;

        @Check(name = "备注")
        private String remark;

        @Check(name = "状态")
        private Integer status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIpAdd() {
            return ipAdd;
        }

        public void setIpAdd(String ipAdd) {
            this.ipAdd = ipAdd;
        }

        public String getPubKey() {
            return pubKey;
        }

        public void setPubKey(String pubKey) {
            this.pubKey = pubKey;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }


}

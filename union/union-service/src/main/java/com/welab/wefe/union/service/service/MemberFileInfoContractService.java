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

package com.welab.wefe.union.service.service;

import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.data.mongodb.entity.union.MemberFileInfo;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.DateUtil;
import com.welab.wefe.common.util.JObject;
import com.welab.wefe.common.util.StringUtil;
import com.welab.wefe.union.service.common.BlockChainContext;
import com.welab.wefe.union.service.contract.MemberContract;
import com.welab.wefe.union.service.contract.MemberFileInfoContract;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuxin.zhang
 */
@Service
public class MemberFileInfoContractService extends AbstractContractService {
    private static final Logger LOG = LoggerFactory.getLogger(MemberFileInfoContractService.class);

    /**
     * add memberFileInfo
     */
    public void add(MemberFileInfo memberFileInfo) throws StatusCodeWithException {
        try {
            // get contract
            MemberFileInfoContract memberContract = getContract();
            // send transaction
            TransactionReceipt transactionReceipt = memberContract.insert(
                    generateParams(memberFileInfo),
                    JObject.create(memberFileInfo.getExtJson()).toJSONString()
            );

            // get receipt result
            TransactionResponse transactionResponse = BlockChainContext.getInstance().getUnionTransactionDecoder()
                    .decodeReceiptWithValues(MemberContract.ABI, MemberContract.FUNC_INSERT, transactionReceipt);

            LOG.info("MemberFileInfo contract insert transaction, memberFileInfo id: {},  receipt response: {}", memberFileInfo.getId(), JObject.toJSON(transactionResponse).toString());

            transactionIsSuccess(transactionResponse);

        } catch (StatusCodeWithException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("add memberFileInfo error: ", e);
            throw new StatusCodeWithException("add memberFileInfo error: ", StatusCode.SYSTEM_ERROR);
        }
    }


    /**
     * Check if the member file information exists
     *
     * @return true：Exists, false: not exist
     */
    public boolean isExist(String id) throws StatusCodeWithException {
        try {
            MemberFileInfoContract memberContract = getContract();
            Boolean ret = memberContract.isExist(id);
            return (null != ret && ret);
        } catch (Exception e) {
            LOG.error("Check if the member file information exists failed: ", e);
            throw new StatusCodeWithException("Check if the member file information exists failed: ", StatusCode.SYSTEM_ERROR);
        }
    }


    /**
     * get contract
     */
    private MemberFileInfoContract getContract() throws StatusCodeWithException {
        BlockChainContext blockChainContext = BlockChainContext.getInstance();
        return blockChainContext.getLatestVersionMemberFileInfoContract();
    }


    private List<String> generateParams(MemberFileInfo memberFileInfo) {
        List<String> list = new ArrayList<>();
        list.add(memberFileInfo.getFileId());
        list.add(memberFileInfo.getFileSign());
        list.add(memberFileInfo.getFileName());
        list.add(memberFileInfo.getFileSize());
        list.add(memberFileInfo.getMemberId());
        list.add(memberFileInfo.getReporter());
        list.add(memberFileInfo.getPurpose());
        list.add(memberFileInfo.getDescribe());
        list.add(memberFileInfo.getEnable());
        list.add(StringUtil.isEmptyToBlank(DateUtil.toStringYYYY_MM_DD_HH_MM_SS2(new Date())));
        list.add(StringUtil.isEmptyToBlank(DateUtil.toStringYYYY_MM_DD_HH_MM_SS2(new Date())));
        return list;
    }
}

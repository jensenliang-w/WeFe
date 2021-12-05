package com.welab.wefe.union.service.service;

import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.data.mongodb.entity.union.UnionNode;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.JObject;
import com.welab.wefe.union.service.contract.UnionNodeContract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderService;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuxin.zhang
 **/
@Service
public class UnionNodeContractService extends AbstractContractService {
    private static final Logger LOG = LoggerFactory.getLogger(UnionNodeContractService.class);

    @Autowired
    private UnionNodeContract unionNodeContract;
    @Autowired
    private CryptoSuite cryptoSuite;

    /**
     * add UnionNode
     */
    public void add(UnionNode unionNode) throws StatusCodeWithException {
        try {
            // send transaction
            TransactionReceipt transactionReceipt = unionNodeContract.insert(
                    generateAddParams(unionNode),
                    JObject.toJSONString(unionNode.getExtJson())
            );

            // get receipt result
            TransactionResponse transactionResponse = new TransactionDecoderService(cryptoSuite)
                    .decodeReceiptWithValues(UnionNodeContract.ABI, UnionNodeContract.FUNC_INSERT, transactionReceipt);


            LOG.info("UnionNode contract insert transaction, unionBaseUrl: {},  receipt response: {}", unionNode.getBaseUrl(), JObject.toJSON(transactionResponse).toString());

            transactionIsSuccess(transactionResponse);

        } catch (StatusCodeWithException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("add UnionNode error: ", e);
            throw new StatusCodeWithException("add UnionNode error: ", StatusCode.SYSTEM_ERROR);
        }
    }

    private List<String> generateAddParams(UnionNode unionNode) {
        List<String> list = new ArrayList<>();
        list.add(unionNode.getNodeId());
        list.add(unionNode.getBlockchainNodeId());
        list.add(unionNode.getBaseUrl());
        list.add(unionNode.getOrganizationName());
        list.add(unionNode.getLostContact());
        list.add(unionNode.getContactEmail());
        list.add(unionNode.getPriorityLevel());
        list.add(unionNode.getVersion());
        list.add(unionNode.getCreatedTime());
        list.add(unionNode.getUpdatedTime());
        return list;
    }

}

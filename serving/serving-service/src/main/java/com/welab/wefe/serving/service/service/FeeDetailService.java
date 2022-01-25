/*
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

import com.welab.wefe.serving.service.api.feedetail.QueryListApi;
import com.welab.wefe.serving.service.database.serving.entity.FeeDetailMysqlModel;
import com.welab.wefe.serving.service.database.serving.entity.FeeDetailOutputModel;
import com.welab.wefe.serving.service.database.serving.repository.FeeDetailRepository;
import com.welab.wefe.serving.service.database.serving.repository.FeeRecordRepository;
import com.welab.wefe.serving.service.dto.PagingOutput;
import com.welab.wefe.serving.service.enums.QueryDateTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeeDetailService {

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    @Autowired
    private FeeDetailRepository feeDetailRepository;

    public void save(FeeDetailMysqlModel input) {

        FeeDetailMysqlModel model = feeDetailRepository.findOne("id", input.getId(), FeeDetailMysqlModel.class);
        if (null == model) {
            model = new FeeDetailMysqlModel();
        }

        model.setServiceId(input.getServiceId());
        model.setClientId(input.getClientId());
        model.setTotalFee(input.getTotalFee());
        model.setTotalRequestTimes(input.getTotalRequestTimes());
        model.setCreatedTime(new Date());

        feeDetailRepository.save(model);
    }



    public PagingOutput<FeeDetailOutputModel> queryList(QueryListApi.Input input) {
        List<FeeDetailOutputModel> models = null;
        Integer total = 0;
        if (input.getQueryDateType() == null || input.getQueryDateType() == QueryDateTypeEnum.HOUR.getValue()) {
            models = feeRecordRepository.queryList(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y-%m-%d %H:00:00",
                    input.getStartTime(), input.getEndTime(), input.getPageIndex() * input.getPageSize(), input.getPageSize());
            total = feeRecordRepository.count(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y-%m-%d %H:00:00",
                    input.getStartTime(), input.getEndTime());
        } else if (input.getQueryDateType() == QueryDateTypeEnum.YEAR.getValue()) {

            models = feeRecordRepository.queryList(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y", input.getStartTime(), input.getEndTime(), input.getPageIndex() * input.getPageSize(), input.getPageSize());
            total = feeRecordRepository.count(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y",
                    input.getStartTime(), input.getEndTime());
        } else if (input.getQueryDateType() == QueryDateTypeEnum.DAY.getValue()) {
            models = feeRecordRepository.queryList(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y-%m-%d", input.getStartTime(), input.getEndTime(), input.getPageIndex() * input.getPageSize(), input.getPageSize());
            total = feeRecordRepository.count(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y-%m-%d",
                    input.getStartTime(), input.getEndTime());
        } else if (input.getQueryDateType() == QueryDateTypeEnum.MONTH.getValue()) {
            models = feeRecordRepository.queryList(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y-%m", input.getStartTime(), input.getEndTime(), input.getPageIndex() * input.getPageSize(), input.getPageSize());
            total = feeRecordRepository.count(input.getClientName(), input.getServiceName(),
                    input.getServiceType(), "%Y-%m",
                    input.getStartTime(), input.getEndTime());
        }


        return PagingOutput.of(total == null ? 0 : total, models);
    }

    /**
     * 该接口用于统计
     *
     * @param input
     * @return
     */
//    public PagingOutput<FeeDetailMysqlModel> queryStatistics(QueryListApi.Input input) {
//
//        // return result
//        List<FeeDetailOutputModel> list = new ArrayList<>();
//
//        Specification<FeeDetailMysqlModel> feeDetailMysqlModelSpecification = Where.create()
//                .contains("serviceName", input.getServiceName())
//                .contains("clientName", input.getClientName())
//                .equal("serviceType", input.getServiceType())
//                .betweenAndDate("createdTime", input.getStartTime(), input.getEndTime())
//                .build(FeeDetailMysqlModel.class);
//
//        List<FeeDetailMysqlModel> feeDetailMysqlModels = feeDetailRepository.findAll(feeDetailMysqlModelSpecification);
////            feeDetailMysqlModels.stream()
////                    .collect(Collectors.groupingBy(feeDetailMysqlModel -> DateUtil.toString(feeDetailMysqlModel.getCreatedTime(), "%Y-%m-%d %H:00:00")))
////                    .forEach((k, v) -> {
////                        v.stream().reduce((v1, v2) -> {
////                            v1.setTotalFee(v1.getTotalFee().add(v2.getTotalFee()));
////                            v1.setTotalRequestTimes(v1.getTotalRequestTimes() + v2.getTotalRequestTimes());
////                            return v1;
////                        });
////                    });
//
//        Map<String, Map<String, List<FeeDetailMysqlModel>>> collect = feeDetailMysqlModels.stream().collect(
//                Collectors.groupingBy(
//                        FeeDetailMysqlModel::getFeeConfigId,
//                        Collectors.groupingBy(
//                                item -> {
//                                    if (input.getQueryDateType() == null || input.getQueryDateType() == QueryDateTypeEnum.HOUR.getValue()) {
//                                        // 按小时分组
//                                        return DateUtil.toString(item.getCreatedTime(), "%Y-%m-%d %H:00:00");
//                                    } else if (input.getQueryDateType() == QueryDateTypeEnum.YEAR.getValue()) {
//                                        // 按年分组
//                                        return DateUtil.toString(item.getCreatedTime(), "%Y");
//                                    } else if (input.getQueryDateType() == QueryDateTypeEnum.DAY.getValue()) {
//                                        // 按日分组
//                                        return DateUtil.toString(item.getCreatedTime(), "%Y-%m-%d");
//                                    } else {
//                                        // 按月分组
//                                        return DateUtil.toString(item.getCreatedTime(), "%Y-%m");
//                                    }
//                                }
//                        )));
//
//        return null;
//
//    }

}

package com.welab.wefe.serving.service.database.serving.repository;

import com.welab.wefe.serving.service.database.serving.entity.RequestStatisticsMysqlModel;
import com.welab.wefe.serving.service.database.serving.repository.base.BaseRepository;
import com.welab.wefe.serving.service.dto.PagingOutput;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * @author ivenn.zheng
 */
@Repository
public interface RequestStatisticsRepository extends BaseRepository<RequestStatisticsMysqlModel, String> {

    /**
     * 根据入参查询相应的接口请求统计结果
     *
     * @param serviceId
     * @param clientId
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "select rand() as id , s.name as serviceName, c.name as clientName, t.total_spend as totalSpend, " +
            "t.success_request as totalSuccessTimes, t.total_request as totalRequestTimes, t.total_request - t.success_request as totalFailTimes, " +
            "s.service_type as serviceType, fc.unit_price as unitPrice " +
            "from ( " +
            "SELECT sum(arr.spend) total_spend, sum(arr.request_result) success_request, count(id) total_request, arr.client_id, arr.service_id " +
            "from api_request_record arr " +
            "where if(:service_id !='', arr.service_id = :service_id ,1=1) and " +
            "      if(:client_id !='', arr.client_id = :client_id ,1=1) and " +
            "      arr.created_time  between if(:start_date !='', :start_date , '1900-01-01 00:00:00' ) and " +
            "      if(:end_date !='', :end_date , NOW())  " +
            "group by arr.service_id, arr.client_id " +
            ")as t left join service s on t.service_id = s.id " +
            "      left join client c on t.client_id = c.id " +
            "      left join fee_config fc on fc.service_id = t.service_id and fc.client_id = t.client_id ", nativeQuery = true, countProjection = "1")
    List<RequestStatisticsMysqlModel> groupByServiceIdAndClientId(@Param("service_id") String serviceId,
                                                                  @Param("client_id") String clientId,
                                                                  @Param("start_date") Date startDate,
                                                                  @Param("end_date") Date endDate);

}

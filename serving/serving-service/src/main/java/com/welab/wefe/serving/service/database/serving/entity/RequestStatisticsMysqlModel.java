package com.welab.wefe.serving.service.database.serving.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author ivenn.zheng
 */
@Entity
public class RequestStatisticsMysqlModel{

    @Id
    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    /**
     * 总调用次数
     */
    private Integer totalRequestTimes;

    /**
     * 总失败次数
     */
    private Integer totalFailTimes;

    /**
     * 总成功次数
     */
    private Integer totalSuccessTimes;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 服务类型
     */
    private Integer serviceType;

    /**
     * 单价
     */
    private Double unitPrice;

    /**
     * 总耗时
     */
    private long totalSpend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(long totalSpend) {
        this.totalSpend = totalSpend;
    }

    public Integer getTotalRequestTimes() {
        return totalRequestTimes;
    }

    public void setTotalRequestTimes(Integer totalRequestTimes) {
        this.totalRequestTimes = totalRequestTimes;
    }

    public Integer getTotalFailTimes() {
        return totalFailTimes;
    }

    public void setTotalFailTimes(Integer totalFailTimes) {
        this.totalFailTimes = totalFailTimes;
    }

    public Integer getTotalSuccessTimes() {
        return totalSuccessTimes;
    }

    public void setTotalSuccessTimes(Integer totalSuccessTimes) {
        this.totalSuccessTimes = totalSuccessTimes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

}

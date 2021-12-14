-- 服务表
DROP TABLE IF EXISTS service;
CREATE TABLE `service` (
                           `id` varchar(32) NOT NULL COMMENT '全局唯一标识',
                           `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                           `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                           `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `name` varchar(32) NOT NULL COMMENT '服务名',
                           `url` varchar(128) NOT NULL COMMENT '服务地址',
                           `service_type` varchar(32) NOT NULL COMMENT '服务类型  1匿踪查询，2交集查询，3安全聚合',
                           `query_params` text COMMENT '查询参数配置',
                           `data_source` text COMMENT 'SQL配置',
                           `condition_fields` text COMMENT '查询字段',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `url_unique` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务';


-- 客户表
DROP TABLE IF EXISTS client;
CREATE TABLE client(
                       id VARCHAR(32) NOT NULL   COMMENT '客户id' ,
                       name VARCHAR(255) NOT NULL   COMMENT '客户名称' ,
                       created_by varchar(32) DEFAULT NULL COMMENT '创建人',
                       created_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       updated_by varchar(32) DEFAULT NULL COMMENT '更新人',
                       updated_time datetime DEFAULT NULL COMMENT '更新时间',
                       email VARCHAR(255)    COMMENT '邮箱' ,
                       ip_add VARCHAR(255) NOT NULL   COMMENT 'ip地址' ,
                       pub_key VARCHAR(255) NOT NULL   COMMENT '公钥' ,
                       remark VARCHAR(255)    COMMENT '备注' ,
                       PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户基本信息表';


-- 客户-服务表
DROP TABLE IF EXISTS client_service;
CREATE TABLE client_service(
                               id VARCHAR(32) NOT NULL   COMMENT '客户服务id' ,
                               service_id VARCHAR(32) NOT NULL   COMMENT '服务id' ,
                               client_id VARCHAR(32) NOT NULL   COMMENT '客户id' ,
                               created_by varchar(32) DEFAULT NULL COMMENT '创建人',
                               created_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               updated_by varchar(32) DEFAULT NULL COMMENT '更新人',
                               updated_time datetime DEFAULT NULL COMMENT '更新时间',
                               status VARCHAR(255) NOT NULL   COMMENT '是否启用' ,
                               PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT = '客户服务表';
CREATE UNIQUE INDEX service_client_index ON client_service(service_id,client_id);

-- 计费规则配置表
DROP TABLE IF EXISTS fee_config;
CREATE TABLE fee_config(
                           id VARCHAR(32) NOT NULL   COMMENT '' ,
                           service_id VARCHAR(32) NOT NULL   COMMENT '服务id' ,
                           client_id VARCHAR(32) NOT NULL   COMMENT '客户id' ,
                           created_by varchar(32) DEFAULT NULL COMMENT '创建人',
                           created_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           updated_by varchar(32) DEFAULT NULL COMMENT '更新人',
                           updated_time datetime DEFAULT NULL COMMENT '更新时间',
                           unit_price DECIMAL(24,6) NOT NULL   COMMENT '调用单价' ,
                           PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '计费配置';
CREATE INDEX service_client_index ON fee_config(service_id,client_id);

-- API 调用统计表
DROP TABLE IF EXISTS api_call_record;
CREATE TABLE api_call_record(
                                id VARCHAR(32) NOT NULL   COMMENT '租户号' ,
                                service_id VARCHAR(32) NOT NULL   COMMENT '服务id' ,
                                client_id VARCHAR(32) NOT NULL   COMMENT '客户id' ,
                                created_by varchar(32) DEFAULT NULL COMMENT '创建人',
                                created_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_by varchar(32) DEFAULT NULL COMMENT '更新人',
                                updated_time datetime DEFAULT NULL COMMENT '更新时间',
                                call_success_times INT    COMMENT '提交成功次数' ,
                                call_fail_times INT    COMMENT '提交失败次数' ,
                                call_total_times INT    COMMENT '总提交次数' ,
                                PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = 'API 调用记录';
CREATE UNIQUE INDEX service_client_index ON api_call_record(service_id,client_id);

-- 计费详情表
DROP TABLE IF EXISTS fee_detail;
CREATE TABLE fee_detail(
                           id VARCHAR(32) NOT NULL   COMMENT '' ,
                           service_id VARCHAR(32) NOT NULL   COMMENT '服务id' ,
                           client_id VARCHAR(32) NOT NULL   COMMENT '客户id' ,
                           fee_config_id VARCHAR(32) NOT NULL   COMMENT '计费配置id' ,
                           api_call_record_id VARCHAR(32) NOT NULL   COMMENT 'API 调用记录id' ,
                           total_fee DECIMAL(24,6)    COMMENT '总费用' ,
                           created_by varchar(32) DEFAULT NULL COMMENT '创建人',
                           created_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           updated_by varchar(32) DEFAULT NULL COMMENT '更新人',
                           updated_time datetime DEFAULT NULL COMMENT '更新时间',
                           PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '结算详情表';
CREATE UNIQUE INDEX fee_detail_index ON fee_detail(service_id,client_id,fee_config_id,api_call_record_id);

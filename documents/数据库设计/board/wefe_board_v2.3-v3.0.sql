-- 此文件为 v2.3 升级为 v3.0 时需要执行的 sql
-- 请大家按格式写好注释和作者


-- ---------------------------------------
-- v3.0 加表
-- author: zane.luo
-- -------------------------------------
DROP TABLE IF EXISTS `data_resource`;
CREATE TABLE `data_resource`
(
    `id`                      varchar(32)   NOT NULL COMMENT '全局唯一标识',
    `created_by`              varchar(32) COMMENT '创建人',
    `created_time`            datetime(6) NOT NULL default CURRENT_TIMESTAMP (6) COMMENT '创建时间',
    `updated_by`              varchar(32) COMMENT '更新人',
    `updated_time`            datetime(6) COMMENT '更新时间',
    `name`                    varchar(256)  NOT NULL COMMENT '资源名称',
    `description`             varchar(3072) COMMENT '描述',
    `tags`                    varchar(128) COMMENT '标签',
    `storage_type`            varchar(32) COMMENT '存储类型',
    `storage_namespace`       varchar(1000) NOT NULL COMMENT '资源在存储中的命名空间（库名、目录路径）',
    `storage_resource_name`   varchar(1000) NOT NULL COMMENT '资源在存储中的名称（表名、文件名）',
    `total_data_count`        bigint(20) NOT NULL COMMENT '总数据量',
    `public_level`            varchar(32) COMMENT '资源的可见性',
    `public_member_list`      varchar(3072) COMMENT '可见成员列表 只有在列表中的联邦成员才可以看到该资源的基本信息',
    `usage_count_in_job`      int(11) NOT NULL DEFAULT '0' COMMENT '该资源在多少个job中被使用',
    `usage_count_in_flow`     int(11) NOT NULL DEFAULT '0' COMMENT '该资源在多少个flow中被使用',
    `usage_count_in_project`  int(11) NOT NULL DEFAULT '0' COMMENT '该资源在多少个project中被使用',
    `usage_count_in_member`   int(11) NOT NULL DEFAULT '0' COMMENT '该资源被多少个其他成员被使用',
    `derived_resource`        bool default false comment '是否是衍生资源',
    `derived_from`            varchar(32) COMMENT '衍生来源，枚举（原始、对齐、分箱）',
    `derived_from_flow_id`    varchar(64) COMMENT '衍生来源流程id',
    `derived_from_job_id`     varchar(64) COMMENT '衍生来源任务id',
    `derived_from_task_id`    varchar(100) COMMENT '衍生来源子任务id',
    `statistical_information` longtext COMMENT '该数据资源相关的统计信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='数据资源';

DROP TABLE IF EXISTS `data_resource_upload_progress`;
CREATE TABLE `data_resource_upload_progress`
(
    `id`                      varchar(32) NOT NULL COMMENT '全局唯一标识',
    `created_by`              varchar(32) COMMENT '创建人',
    `created_time`            datetime(6) NOT NULL default CURRENT_TIMESTAMP (6) COMMENT '创建时间',
    `updated_by`              varchar(32) COMMENT '更新人',
    `updated_time`            datetime(6) COMMENT '更新时间',
    `data_resource_id`        varchar(32)  DEFAULT NULL COMMENT '数据资源id',
    `data_resource_name`      varchar(128) DEFAULT NULL COMMENT '数据资源名称',
    `total_data_count`        bigint(20) DEFAULT NULL COMMENT '总数据行数',
    `completed_data_count`    bigint(20) DEFAULT NULL COMMENT '已写入数据行数',
    `progress_ratio`          int(10) DEFAULT NULL COMMENT '任务进度百分比',
    `estimate_remaining_time` int(64) DEFAULT NULL COMMENT '预计剩余耗时',
    `invalid_data_count`      int(64) DEFAULT 0 COMMENT '无效数据量（主键重复条数）',
    `error_message`           text         DEFAULT NULL COMMENT '错误消息',
    `status`                  varchar(32) NOT NULL COMMENT '状态：上传中、已完成、已失败',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_unique_data_set_task` (`data_resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
  DEFAULT CHARSET = utf8mb4 COMMENT ='数据资源上传进度';

DROP TABLE IF EXISTS `image_data_set`;
CREATE TABLE `image_data_set`
(
    `id`              varchar(32) NOT NULL COMMENT '全局唯一标识',
    `created_by`      varchar(32) COMMENT '创建人',
    `created_time`    datetime(6) NOT NULL default CURRENT_TIMESTAMP (6) COMMENT '创建时间',
    `updated_by`      varchar(32) COMMENT '更新人',
    `updated_time`    datetime(6) COMMENT '更新时间',
    `for_job_type`    varchar(32) COMMENT '任务类型（物体检测...）',
    `label_list`      varchar(1000) COMMENT 'label 列表',
    `labeled_count`   bigint(20) NOT NULL COMMENT '已标注数量',
    `label_completed` bool COMMENT '是否已标注完毕',
    `files_size`      bigint(20) NOT NULL DEFAULT '0' COMMENT '数据集大小',

    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='图片数据集';



DROP TABLE IF EXISTS `table_data_set`;
CREATE TABLE `table_data_set`
(
    `id`                      varchar(32) NOT NULL COMMENT '全局唯一标识',
    `created_by`              varchar(32) COMMENT '创建人',
    `created_time`            datetime(6) NOT NULL default CURRENT_TIMESTAMP (6) COMMENT '创建时间',
    `updated_by`              varchar(32) COMMENT '更新人',
    `updated_time`            datetime(6) COMMENT '更新时间',
    `column_name_list`        text        NOT NULL COMMENT '数据集字段列表',
    `column_count`            int(11) NOT NULL COMMENT '数据集列数',
    `primary_key_column`      varchar(32) NOT NULL COMMENT '主键字段',
    `feature_name_list`       text COMMENT '特征列表',
    `feature_count`           int(11) NOT NULL COMMENT '特征数量',
    `contains_y`              bool        NOT NULL COMMENT '是否包含 Y 值',
    `y_name_list`             text COMMENT 'y列名称列表',
    `y_count`                 int(11) NOT NULL COMMENT 'y列的数量',
    `positive_sample_value`   varchar(32) COMMENT '正样本的值',
    `y_positive_sample_count` bigint(20) COMMENT '正例数量',
    `y_positive_sample_ratio` double(10, 4
) COMMENT '正例比例',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='数据集';

DROP TABLE IF EXISTS `bloom_filter`;
CREATE TABLE `bloom_filter`
(
    `id`               varchar(32) NOT NULL COMMENT '全局唯一标识',
    `created_by`       varchar(32) COMMENT '创建人',
    `created_time`     datetime(6) NOT NULL default CURRENT_TIMESTAMP (6) COMMENT '创建时间',
    `updated_by`       varchar(32) COMMENT '更新人',
    `updated_time`     datetime(6) COMMENT '更新时间',
    `rsa_e`            text COMMENT '密钥e',
    `rsa_n`            text COMMENT '密钥n',
    `rsa_d`            text COMMENT '密钥e',
    `data_source_id`   varchar(32)  DEFAULT NULL COMMENT '数据源id',
    `source_path`      varchar(255) DEFAULT NULL COMMENT '数据源地址',
    `column_name_list` text COMMENT '选取生成过滤器的字段',
    `add_method`       varchar(255) DEFAULT NULL COMMENT '布隆过滤器添加方式',
    `sql_script`       varchar(255) DEFAULT NULL COMMENT 'sql语句',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='布隆过滤器';


-- -------------------------------------
-- project 表增加字段
-- author: zane.luo
-- -------------------------------------
ALTER TABLE `project`
    ADD COLUMN `project_type` varchar(36) NOT NULL DEFAULT 'MachineLearning' COMMENT '项目类型' AFTER `flow_status_statistics`;

-- -------------------------------------
-- project_data_set 表增加字段
-- author: zane.luo
-- -------------------------------------
ALTER TABLE `project_data_set`
    ADD COLUMN `data_set_type` varchar(36) NOT NULL DEFAULT 'TableDataSet' COMMENT '数据集类型' AFTER `source_task_id`;



DROP TABLE IF EXISTS `job_apply_result`;
CREATE TABLE `job_apply_result`
(
    `id`               varchar(32) NOT NULL COMMENT '全局唯一标识',
    `created_by`       varchar(32) COMMENT '创建人',
    `created_time`     datetime(6) NOT NULL default CURRENT_TIMESTAMP (6) COMMENT '创建时间',
    `updated_by`       varchar(32) COMMENT '更新人',
    `updated_time`     datetime(6) COMMENT '更新时间',
    `job_id`           varchar(255) DEFAULT NULL COMMENT 'jobid',
    `task_id`          varchar(255) DEFAULT NULL COMMENT 'taskid',
    `server_endpoint`  varchar(255) DEFAULT NULL COMMENT '',
    `aggregator_endpoint`   varchar(32)  DEFAULT NULL COMMENT '',
    `aggregator_assignee`      varchar(255) DEFAULT NULL COMMENT '',
    `status` varchar(255) DEFAULT NULL COMMENT '状态',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='深度学习任务申请结果';


INSERT INTO `global_config` (`id`, `created_by`, `created_time`, `updated_by`, `updated_time`, `group`, `name`, `value`, `comment`)
VALUES
	('07ab31c0f41e45e2998d0315fbaac7ab', NULL, '2021-12-16 10:34:30.725000', NULL, '2021-12-16 10:34:30.725000', 'wefe_flow', 'visual_fl_base_url', 'http://10.90.0.86:10002', NULL);

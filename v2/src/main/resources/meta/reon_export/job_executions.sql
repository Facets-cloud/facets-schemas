
-- start  Schema : job_executions

CREATE TABLE `job_executions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `job_group_id` bigint(20) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `template_id` int(11) NOT NULL,
  `template_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_period` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `query_sql` longtext COLLATE utf8mb4_unicode_ci,
  `internal_s3_location` text COLLATE utf8mb4_unicode_ci,
  `internal_s3_done_time` datetime DEFAULT NULL,
  `file_location` text COLLATE utf8mb4_unicode_ci,
  `file_password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `customers_group_s3_location` text COLLATE utf8mb4_unicode_ci,
  `status` enum('IN_PROGRESS','SUCCESS','FAILED','FTP_FAILURE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `substatus` enum('IN_PROGRESS','SUCCESS','FTP_CREDENTIAL_ISSUE','INTERNAL_S3_DONE','INTERNAL_S3_FAILED','META_DATA_ISSUE','QUERY_GENERATION_ISSUE','DUMP_FAILED','TRIGGER_FAILED','FAILED','PERMANENT_META_DATA_ISSUE','PERMANENT_FTP_FAILURE','FTP_ACCESS_ISSUE','FTP_FAILURE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `export_data_start_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `export_data_end_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `trigger_time` datetime NOT NULL,
  `execution_id` bigint(20) NOT NULL,
  `external_target_file_path` text COLLATE utf8mb4_unicode_ci,
  `priority` tinyint(3) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `execution` (`org_id`,`job_group_id`,`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=98752 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : job_executions
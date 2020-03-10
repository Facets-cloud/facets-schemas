
-- start  Schema : jobs

CREATE TABLE `jobs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `export_request_id` bigint(20) NOT NULL,
  `request_set_id` bigint(20) DEFAULT NULL,
  `status` enum('QUEUED','QUERY_GEN_SUCCESS','QUERY_GEN_FAILED','QUERY_EXEC_SUCCESS','QUERY_EXEC_FAILED','SUCCESS','FAILED','QUERY_EXEC_IN_PROGRESS','CANCELLED','UPLOADING_FILE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `mdw_properties` longtext COLLATE utf8mb4_unicode_ci,
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_service_handle` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_s3_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_version` smallint(5) DEFAULT NULL,
  `file_sort_column` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ddl_query` longtext COLLATE utf8mb4_unicode_ci,
  `dml_query` mediumtext COLLATE utf8mb4_unicode_ci,
  `mdw_sync_time` timestamp NULL DEFAULT NULL,
  `soft_deadline` timestamp NULL DEFAULT NULL,
  `error_message` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_details` longtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `export_request_idx` (`export_request_id`),
  KEY `status_idx` (`org_id`,`status`),
  KEY `status_x` (`status`,`soft_deadline`)
) ENGINE=InnoDB AUTO_INCREMENT=207099 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : jobs
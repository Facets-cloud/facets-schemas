
-- start  Schema : import_files_history

CREATE TABLE `import_files_history` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `org_id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `filename` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `namespace` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `count` int(10) NOT NULL,
  `template_id` int(10) NOT NULL,
  `profile_id` int(10) NOT NULL,
  `is_validated` int(10) NOT NULL,
  `temp_table_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `temp_table_count` int(10) NOT NULL,
  `csv_properties` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `time_taken` datetime NOT NULL,
  `import_status` enum('SUCCESS','FAIL','INPROGRESS','PREVIEWED','TEMP_DB_COMPLETED','POINT_ENGINE_PENDING','POINT_ENGINE_INPROGRESS','POINT_ENGINE_FAIL','CANCELED','DELETED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `memory_consumed` double DEFAULT NULL,
  `memory_peak` double DEFAULT NULL,
  `cpu_usage` double DEFAULT NULL,
  `task_time_breakup` mediumtext COLLATE utf8mb4_unicode_ci,
  `query_time_breakup` mediumtext COLLATE utf8mb4_unicode_ci,
  `additional_info` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Json formatted all misc information saved here',
  PRIMARY KEY (`id`),
  KEY `org` (`org_id`),
  KEY `user` (`user_id`),
  KEY `status_idx` (`import_status`),
  KEY `start_time_idx` (`start_time`),
  KEY `end_time_idx` (`end_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_files_history
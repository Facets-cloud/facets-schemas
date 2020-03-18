
-- start  Schema : store_server_sync_logs

CREATE TABLE `store_server_sync_logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `log_sync_type` enum('CUSTOMERS','CUSTOM_FIELDS_DATA','VOUCHER_SERIES','VOUCHERS','INVENTORY_MASTER','CUSTOMER_ATTRIBUTES','STORE_ATTRIBUTES','LOYALTY_TRACKER','FRAUD_USERS_LIST','TASK_METADATA','TASKS','ASSOCIATES','REMINDERS','COMM_TEMPLATE','STORES') COLLATE utf8mb4_unicode_ci NOT NULL,
  `sync_status` enum('DOWNLOADING','DOWNLOAD_FAILED','DOWNLOAD_COMPLETE','DATA_POPULATION','INDEXING','COMPLETED','NONE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_full_sync_time` datetime DEFAULT NULL,
  `read_time` int(11) DEFAULT '-1' COMMENT 'time to read in seconds',
  `file_size` float DEFAULT '-1' COMMENT 'file size in kb',
  `unzipping_time` int(11) DEFAULT '-1' COMMENT 'time to unzip in seconds',
  `indexing_time` int(11) DEFAULT '-1' COMMENT 'time to index in seconds',
  `request_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_full_sync` tinyint(1) DEFAULT NULL,
  `avg_mem_usage` float DEFAULT '-1' COMMENT 'avg memory usage in this sync',
  `peak_mem_usage` float DEFAULT '-1' COMMENT 'peak memory usage in this sync',
  `avg_cpu_usage` float DEFAULT '-1' COMMENT 'average cpu usage in this sync',
  `peak_cpu_usage` float DEFAULT '-1' COMMENT 'peak cpi usage in this sync',
  `last_updated_at` datetime DEFAULT NULL COMMENT 'record insert time',
  `last_delta_sync_time` datetime DEFAULT NULL,
  `ss_health_fkey` int(11) DEFAULT '-1' COMMENT 'fkey from store_server_health table',
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `last_updated_at` (`last_updated_at`),
  KEY `ssfkey` (`ss_health_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=64652541 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_server_sync_logs
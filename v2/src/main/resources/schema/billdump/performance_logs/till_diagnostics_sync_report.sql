
-- start  Schema : till_diagnostics_sync_report

CREATE TABLE `till_diagnostics_sync_report` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `till_diagnostics_fkey` int(11) NOT NULL COMMENT 'PKEY from from till_diagnostics table',
  `sync_type` enum('CUSTOMERS','CUSTOM_FIELDS_DATA','VOUCHER_SERIES','VOUCHERS','INVENTORY_MASTER','CUSTOMER_ATTRIBUTES','STORE_ATTRIBUTES','LOYALTY_TRACKER','FRAUD_USERS_LIST','STORE_TASKS','STORE_TASKS_ENTRY','TASK_METADATA','TASKS','ASSOCIATES','REMINDERS','COMM_TEMPLATE','STORES') COLLATE utf8mb4_unicode_ci NOT NULL,
  `sync_status` enum('DOWNLOADING','DOWNLOAD_FAILED','DOWNLOAD_COMPLETE','DATA_POPULATION','INDEXING','COMPLETED','NONE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_sync_time` datetime NOT NULL,
  `last_delta_sync_time` datetime DEFAULT NULL,
  `read_time` int(11) DEFAULT '-1' COMMENT 'time to read in seconds',
  `file_size` float DEFAULT '-1' COMMENT 'file size in kb',
  `unzipping_time` int(11) DEFAULT '-1' COMMENT 'time to unzip in seconds',
  `indexing_time` int(11) DEFAULT '-1' COMMENT 'time to index in seconds',
  `request_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_full_sync` tinyint(1) DEFAULT NULL,
  `avg_mem_usage` float NOT NULL DEFAULT '-1' COMMENT 'avg memory usage in this sync',
  `peak_mem_usage` float NOT NULL DEFAULT '-1' COMMENT 'peak memory usage in this sync',
  `avg_cpu_usage` float NOT NULL DEFAULT '-1' COMMENT 'average cpu usage in this sync',
  `peak_cpu_usage` float NOT NULL DEFAULT '-1' COMMENT 'peak cpi usage in this sync',
  `inserted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `till_diagnostics_fkey` (`till_diagnostics_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=9335223 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : till_diagnostics_sync_report
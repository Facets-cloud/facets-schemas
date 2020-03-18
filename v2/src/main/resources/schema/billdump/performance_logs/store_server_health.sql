
-- start  Schema : store_server_health

CREATE TABLE `store_server_health` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `store_id` int(11) NOT NULL COMMENT 'Store servers entity id',
  `org_id` int(11) NOT NULL,
  `up_time` int(11) DEFAULT '-1' COMMENT 'time in mins since store server is UP',
  `requests_processed` int(11) DEFAULT '-1' COMMENT 'no of requests processed by store server',
  `os` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OS on which store server is installed',
  `os_platform` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OS platform',
  `processor` int(11) DEFAULT '-1' COMMENT 'no of processors',
  `system_ram` int(11) DEFAULT '-1' COMMENT 'System RAM in MB',
  `db_size` int(11) DEFAULT '-1' COMMENT 'local db size in MB rounded off',
  `lan_speed` int(11) DEFAULT '-1' COMMENT 'LAN speed in kbps rounded off',
  `last_transaction_time` datetime DEFAULT NULL COMMENT 'time when last txn is recorded by this store server',
  `avg_mem_usage` float DEFAULT '-1' COMMENT 'avg memory usage by store server',
  `peak_mem_usage` float DEFAULT '-1' COMMENT 'peak memory usage by store server',
  `avg_cpu_usage` float DEFAULT '-1' COMMENT 'average cpu usage by store server',
  `peak_cpu_usage` float DEFAULT '-1' COMMENT 'peak cpi usage by store server',
  `last_updated_at` datetime DEFAULT NULL COMMENT 'record insert time',
  `wcf_version` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_txn_to_svr` datetime DEFAULT NULL,
  `last_regn_to_svr` datetime DEFAULT NULL,
  `report_generation_time` datetime DEFAULT NULL COMMENT 'Time when report on client is compiled',
  `last_login` datetime DEFAULT NULL,
  `last_fullsync` datetime DEFAULT NULL,
  `current_binary_version` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `available_binary_version` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`store_id`),
  KEY `last_updated_at` (`last_updated_at`),
  KEY `org` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=852274 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_server_health
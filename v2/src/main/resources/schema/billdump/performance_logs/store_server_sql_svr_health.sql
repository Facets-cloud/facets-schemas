
-- start  Schema : store_server_sql_svr_health

CREATE TABLE `store_server_sql_svr_health` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `is_alive` tinyint(4) DEFAULT NULL COMMENT 'is sql server alive',
  `last_query_exec_time` datetime DEFAULT NULL COMMENT 'last datetime when server executed query',
  `average_cpu_time` float DEFAULT NULL COMMENT 'avg memory usage in this sync',
  `active_connection_count` int(11) DEFAULT NULL,
  `avg_disk_io` float DEFAULT NULL,
  `total_db_size` int(11) DEFAULT NULL COMMENT 'db size in MB rounded off',
  `intouch_db_size` int(11) DEFAULT NULL COMMENT 'db size in MB rounded off',
  `os` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OS on which SQL server is installed',
  `os_platform` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OS platform',
  `processor` int(11) DEFAULT '-1' COMMENT 'no of processors',
  `system_ram` int(11) DEFAULT '-1' COMMENT 'System RAM in MB',
  `last_updated_at` datetime NOT NULL COMMENT 'record insert time',
  `ss_health_fkey` int(11) DEFAULT '-1' COMMENT 'fkey from store_server_health table',
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `last_updated_at` (`last_updated_at`),
  KEY `ssfkey` (`ss_health_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=852260 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_server_sql_svr_health
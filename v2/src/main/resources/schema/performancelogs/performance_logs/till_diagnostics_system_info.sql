
-- start  Schema : till_diagnostics_system_info

CREATE TABLE `till_diagnostics_system_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `till_diagnostics_fkey` int(11) NOT NULL COMMENT 'PKEY from from till_diagnostics table',
  `os` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OS on which store client is installed',
  `os_platform` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OS platform',
  `processor` int(11) DEFAULT '-1' COMMENT 'no of processors',
  `processor_family` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'code for processor family',
  `system_ram` int(11) DEFAULT '-1' COMMENT 'System RAM in MB',
  `db_size` float DEFAULT '-1',
  `sqlite_version` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `framework_version` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `heartbeat_success` int(11) DEFAULT NULL,
  `heartbeat_failure` int(11) DEFAULT NULL,
  `till_time` datetime DEFAULT NULL,
  `server_time` datetime DEFAULT NULL,
  `timezone` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `proxy_enabled` int(11) NOT NULL DEFAULT '0',
  `inserted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `till_diagnostics_fkey` (`till_diagnostics_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=8665519 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : till_diagnostics_system_info
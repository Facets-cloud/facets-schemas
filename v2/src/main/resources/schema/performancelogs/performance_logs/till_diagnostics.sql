
-- start  Schema : till_diagnostics

CREATE TABLE `till_diagnostics` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `till_id` int(11) DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `from_` datetime DEFAULT NULL COMMENT 'till report collection start time',
  `to_` datetime DEFAULT NULL COMMENT 'till report collection end time',
  `last_login` datetime DEFAULT NULL COMMENT 'last till login time',
  `last_fullsync` datetime DEFAULT NULL COMMENT 'last time when fullsync happened',
  `integration_mode` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'screenscraping, filescrapping etc',
  `current_binary_version` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `available_binary_version` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_skip_count` int(11) DEFAULT NULL COMMENT 'no of times update is skipped at till',
  `last_binary_update` datetime DEFAULT NULL COMMENT 'time when binary was updated',
  `avg_mem_usage` float NOT NULL DEFAULT '-1',
  `peak_mem_usage` float NOT NULL DEFAULT '-1',
  `avg_cpu_usage` float NOT NULL DEFAULT '-1',
  `peak_cpu_usage` float NOT NULL DEFAULT '-1',
  `inserted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`till_id`,`org_id`),
  KEY `org` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8665546 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : till_diagnostics
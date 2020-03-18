
-- start  Schema : migration_log

CREATE TABLE `migration_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `config_file` longtext COLLATE utf8mb4_unicode_ci,
  `type` enum('SCHEDULED','MANUAL','IMPORT','EDIT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `queued_time` datetime DEFAULT NULL,
  `queued_by` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `time_taken` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('QUEUED','PICKED','IN_PROGRESS','SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_message` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : migration_log
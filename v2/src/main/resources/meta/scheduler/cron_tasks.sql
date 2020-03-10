
-- start  Schema : cron_tasks

CREATE TABLE `cron_tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `reference_id` int(11) NOT NULL,
  `component` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron_pattern` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `picked_time` datetime NOT NULL,
  `scheduled_time` datetime NOT NULL,
  `time_zone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('OPENED','CLOSED','EXECUTING','REMINDING','REMINDED','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPENED',
  `error_count` int(11) NOT NULL,
  `params` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`),
  KEY `status` (`status`,`scheduled_time`)
) ENGINE=InnoDB AUTO_INCREMENT=215640 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : cron_tasks

-- start  Schema : cron_task_logs

CREATE TABLE `cron_task_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) DEFAULT NULL,
  `description` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('SUCCESS','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `scheduled_time` datetime NOT NULL,
  `last_updated_time` datetime NOT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `last_updated_time_idx` (`last_updated_time`),
  KEY `ref_status_idx` (`ref_id`,`status`),
  KEY `scheduled_time_idx` (`scheduled_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : cron_task_logs
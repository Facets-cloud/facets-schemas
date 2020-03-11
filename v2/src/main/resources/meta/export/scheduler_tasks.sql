
-- start  Schema : scheduler_tasks

CREATE TABLE `scheduler_tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `reminder_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'cron reminder id for reference',
  `start_date` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `end_date` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'RUNNING',
  `filters` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'json formatted filters to be applied on the task',
  `created_by` int(11) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `period` enum('DAILY','WEEKLY','MONTHLY','TILL_DATE','ONETIME','S3_SYNC') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `additional_info` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `status_idx` (`org_id`,`status`(1)),
  KEY `last_updated_idx` (`org_id`,`last_updated_on`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : scheduler_tasks
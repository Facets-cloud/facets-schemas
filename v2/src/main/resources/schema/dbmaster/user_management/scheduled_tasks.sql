
-- start  Schema : scheduled_tasks

CREATE TABLE `scheduled_tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `filter_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `filter_data` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `filter_args` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `frequency` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `listeners` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `scheduler_task_id` bigint(20) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : scheduled_tasks

-- start  Schema : task_reminder

CREATE TABLE `task_reminder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `created_by` int(11) NOT NULL,
  `remindee_id` int(11) NOT NULL,
  `time` datetime NOT NULL,
  `template` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `store_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : task_reminder
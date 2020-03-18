
-- start  Schema : store_tasks_entries

CREATE TABLE `store_tasks_entries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL DEFAULT '-1',
  `task_created_on` datetime NOT NULL,
  `task_created_by` int(11) NOT NULL,
  `display_title` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `display_text` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `task_status_updated_on` datetime DEFAULT NULL,
  `task_status_update_notes` mediumtext COLLATE utf8mb4_unicode_ci,
  `is_completed` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'is the task entry completed by the store',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`task_id`,`store_id`),
  KEY `task_status_updated_idx` (`org_id`,`store_id`,`task_status_updated_on`),
  KEY `auto_time_idx` (`task_status_updated_on`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Contains the individual tasks to be sent to the stores';


-- end  Schema : store_tasks_entries
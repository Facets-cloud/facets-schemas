
-- start  Schema : store_tasks

CREATE TABLE `store_tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `msg_queue_id` int(11) NOT NULL DEFAULT '-1',
  `task_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_target_type` enum('CUSTOMER','CASHIER') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Whether the task is mean for the cahier or the customer',
  `task_action_type` enum('CALL','PROMOTE','INTERNAL','REMINDER') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'What is the action the cashier has to take',
  `task_start_date` datetime NOT NULL,
  `task_completion_in_days` int(11) NOT NULL,
  `task_display_title` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_display_text` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_priority` int(11) NOT NULL,
  `task_status_options` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Contains the list of status options',
  `task_status_default_option` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'All entries to have this status by default',
  `task_status_completed_option` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Contains the status completed option',
  `params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_enabled` tinyint(1) NOT NULL DEFAULT '1',
  `tasks_entries_created_by` int(11) DEFAULT NULL,
  `tasks_entries_created_on` datetime DEFAULT NULL,
  `created_on` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_on` datetime NOT NULL,
  `modified_by` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Contains the header information of the tasks';


-- end  Schema : store_tasks
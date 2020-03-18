
-- start  Schema : tracker_rollout_log

CREATE TABLE `tracker_rollout_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `program_id` int(11) NOT NULL,
  `tracker_info_id` int(11) NOT NULL,
  `tracker_strategy_id` int(11) NOT NULL,
  `point_category_id` int(11) NOT NULL,
  `listener_id` int(11) DEFAULT NULL,
  `rule_id` int(11) DEFAULT NULL,
  `migration_date` datetime DEFAULT NULL,
  `migrated_to` enum('INACTIVE','PASSIVE','ACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tracker_data_last_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `rollout_log_index` (`org_id`,`tracker_info_id`,`tracker_strategy_id`,`point_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracker_rollout_log
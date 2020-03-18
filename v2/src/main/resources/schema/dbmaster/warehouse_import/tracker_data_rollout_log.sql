
-- start  Schema : tracker_data_rollout_log

CREATE TABLE `tracker_data_rollout_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `tracker_info_id` int(11) DEFAULT NULL,
  `tracker_strategy_id` int(11) DEFAULT NULL,
  `td_start_id` int(11) DEFAULT NULL,
  `td_end_id` int(11) DEFAULT NULL,
  `migration_time` datetime DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`tracker_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracker_data_rollout_log
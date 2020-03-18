
-- start  Schema : customer_points_summary

CREATE TABLE `customer_points_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL COMMENT 'customer whose points summary is being tracked',
  `points_category_id` int(11) NOT NULL COMMENT 'category for the points',
  `current_points` decimal(15,3) NOT NULL,
  `cumulative_points` decimal(15,3) NOT NULL,
  `cumulative_purchases` decimal(15,3) NOT NULL,
  `points_redeemed` decimal(15,3) NOT NULL,
  `points_expired` decimal(15,3) NOT NULL,
  `points_returned` decimal(15,3) NOT NULL,
  `backlog_points` decimal(15,3) DEFAULT NULL,
  `reissued_points` decimal(15,3) DEFAULT NULL,
  `last_awarded_on` datetime DEFAULT NULL COMMENT 'When the last time the point was awarded',
  `last_updated_on` datetime NOT NULL,
  `last_updated_by_till` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`org_id`,`program_id`,`customer_id`,`points_category_id`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : customer_points_summary

-- start  Schema : mlm_awarded_points

CREATE TABLE `mlm_awarded_points` (
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `loyalty_log_id` bigint(20) NOT NULL,
  `awarded_points` int(11) NOT NULL,
  `awarded_time` datetime NOT NULL,
  `user_slab` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Slab name at the time of awarding',
  `distance_from_bill` int(11) NOT NULL,
  `processed_time` datetime DEFAULT NULL,
  PRIMARY KEY (`org_id`,`user_id`,`loyalty_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mlm_awarded_points
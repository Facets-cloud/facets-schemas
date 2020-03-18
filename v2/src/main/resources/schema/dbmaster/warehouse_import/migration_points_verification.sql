
-- start  Schema : migration_points_verification

CREATE TABLE `migration_points_verification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT 'OK',
  `migration_log_id` int(11) DEFAULT NULL,
  `migration_type` enum('M','I') COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `cps_loyalty_points` double(15,3) NOT NULL,
  `cps_lifetime_points` double(15,3) NOT NULL,
  `cps_expired_points` double(15,3) NOT NULL,
  `cps_redeemed_points` double(15,3) NOT NULL,
  `awarded_points` double(15,3) NOT NULL,
  `bill_points` double(15,3) NOT NULL,
  `pe_total_awarded_points` double(15,3) NOT NULL,
  `redeemed_points` double(15,3) NOT NULL,
  `pe_redeemed_points` double(15,3) NOT NULL,
  `expired_points` double(15,3) NOT NULL,
  `pe_expired_points` double(15,3) NOT NULL,
  `return_points` double(15,3) NOT NULL,
  `available_points` double(15,3) NOT NULL,
  `pe_available_points` double(15,3) NOT NULL,
  `diff_loyalty_points` double(15,3) NOT NULL,
  `diff_lifetime_points` double(15,3) NOT NULL,
  `max_pa_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `pa_id` (`migration_log_id`,`org_id`,`user_id`,`max_pa_id`),
  UNIQUE KEY `user_id` (`migration_log_id`,`org_id`,`user_id`),
  KEY `org_id` (`org_id`,`migration_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : migration_points_verification
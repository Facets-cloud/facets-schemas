
-- start  Schema : points_mismatch_summary

CREATE TABLE `points_mismatch_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `verification_log_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `loyalty_points` int(11) NOT NULL,
  `cps_loyalty_points` float NOT NULL,
  `lifetime_points` int(11) NOT NULL,
  `cps_lifetime_points` int(11) NOT NULL,
  `lifetime_purchases` int(11) NOT NULL,
  `cps_lifetime_purchases` int(11) NOT NULL,
  `redeemed_points` int(11) NOT NULL,
  `pe_redeemed_points` float NOT NULL,
  `cps_redeemed_points` float NOT NULL,
  `expired_points` int(11) NOT NULL,
  `pe_expired_points` float NOT NULL,
  `cps_expired_points` float NOT NULL,
  `slab_id` int(11) DEFAULT NULL,
  `pe_slab_id` int(11) NOT NULL,
  `status` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`customer_id`),
  KEY `verification_log_id` (`verification_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_mismatch_summary

-- start  Schema : points_redemption_summary

CREATE TABLE `points_redemption_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `program_id` int(1) NOT NULL,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `point_category_id` int(11) NOT NULL,
  `points_redeemed` decimal(15,3) NOT NULL,
  `redemption_type` enum('REDEMPTION','REVERSAL','GROUP_REDEMPTION','REVERSAL_ON_RETURN') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REDEMPTION',
  `source_type` enum('API','IMPORT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_id` int(11) NOT NULL,
  `redemption_id` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'unique redemption id for points redemption',
  `validation_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bill_id` int(11) NOT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `redemption_time` datetime NOT NULL COMMENT 'time of redemption',
  `points_redemption_time` datetime NOT NULL COMMENT 'time when points redeemed from warehouse',
  `till_id` bigint(20) DEFAULT NULL COMMENT 'counter which did the redemption',
  `request_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `customer_index` (`org_id`,`program_id`,`customer_id`,`redemption_time`,`till_id`) USING BTREE,
  UNIQUE KEY `customer_redemption_idx` (`org_id`,`customer_id`,`redemption_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_redemption_summary
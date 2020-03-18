
-- start  Schema : incentive_meta_details

CREATE TABLE `incentive_meta_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'incentive_type_id for each incentive',
  `incentive_type` enum('POINTS','COUPON','GENERIC') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `label` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `incentive_type` (`incentive_type`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : incentive_meta_details
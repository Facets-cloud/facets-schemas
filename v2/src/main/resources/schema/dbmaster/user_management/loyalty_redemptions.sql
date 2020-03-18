
-- start  Schema : loyalty_redemptions

CREATE TABLE `loyalty_redemptions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loyalty_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `points_redeemed` int(11) NOT NULL,
  `voucher_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `entered_by` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `counter_id` bigint(20) DEFAULT NULL COMMENT 'counter which did the redemption',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `loyalty_id` (`loyalty_id`),
  KEY `org_id` (`org_id`,`entered_by`),
  KEY `org_id_2` (`org_id`,`user_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty_redemptions
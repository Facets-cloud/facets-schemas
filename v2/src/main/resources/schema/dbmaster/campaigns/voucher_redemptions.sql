
-- start  Schema : voucher_redemptions

CREATE TABLE `voucher_redemptions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `voucher_series_id` int(11) DEFAULT NULL,
  `voucher_id` int(11) NOT NULL,
  `used_by` bigint(20) NOT NULL,
  `used_date` datetime NOT NULL,
  `used_at_store` bigint(20) NOT NULL,
  `sales_nextbill` float DEFAULT NULL,
  `sales_sameday` float DEFAULT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `details` text COLLATE utf8mb4_unicode_ci COMMENT 'Any details about the redemption',
  `entry_type` enum('intouch','manual') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'intouch',
  `validation_code_used` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `counter_id` bigint(20) NOT NULL DEFAULT '-1',
  `bill_amount` double NOT NULL DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `voucher_id` (`voucher_id`),
  KEY `org_id` (`org_id`,`voucher_series_id`),
  KEY `org_id_2` (`org_id`,`used_by`,`bill_number`),
  KEY `org_time_idx` (`auto_update_time`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : voucher_redemptions
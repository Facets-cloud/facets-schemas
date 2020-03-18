
-- start  Schema : coupons_created

CREATE TABLE `coupons_created` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `series_expiry_date` datetime NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `campaigns_migrated_voucher_id` int(11) DEFAULT '-1',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `is_queued` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_coupon_code_idx` (`org_id`,`coupon_code`,`is_valid`),
  KEY `org_coupon_series_idx` (`org_id`,`coupon_series_id`,`is_valid`),
  KEY `org_coupon_series_isqueued_idx` (`org_id`,`coupon_series_id`,`is_valid`,`is_queued`),
  KEY `org_coupon_series_code_idx` (`org_id`,`coupon_series_id`,`coupon_code`,`is_valid`),
  KEY `org_id_last_updated_on_idx` (`org_id`,`last_updated_on`),
  KEY `org_id_series_expiry_date_idx` (`org_id`,`series_expiry_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : coupons_created
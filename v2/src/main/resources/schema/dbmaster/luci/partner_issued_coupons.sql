
-- start  Schema : partner_issued_coupons

CREATE TABLE `partner_issued_coupons` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `partner_org_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `coupon_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `issued_by_id` bigint(20) NOT NULL,
  `requested_by_id` bigint(20) DEFAULT '-1',
  `issued_on` datetime NOT NULL,
  `notes` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `coupon_issued_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_coupon_series_idx` (`org_id`,`coupon_series_id`,`is_valid`),
  KEY `org_coupon_series_code_idx` (`org_id`,`coupon_series_id`,`coupon_code`,`is_valid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : partner_issued_coupons
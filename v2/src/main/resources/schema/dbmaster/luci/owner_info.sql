
-- start  Schema : owner_info

CREATE TABLE `owner_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `owned_by` enum('NONE','LOYALTY','OUTBOUND','GOODWILL','DVS','TIMELINE','REFERRAL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `owner_id` int(11) NOT NULL DEFAULT '-1',
  `expiry_date` datetime DEFAULT NULL,
  `modified_by` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_coupon_series_id` (`org_id`,`coupon_series_id`),
  KEY `coupon_series_id` (`org_id`,`coupon_series_id`),
  KEY `auto_update_time` (`org_id`,`auto_update_time`),
  KEY `expiry_date` (`org_id`,`expiry_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : owner_info
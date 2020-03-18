
-- start  Schema : referral_voucher_details

CREATE TABLE `referral_voucher_details` (
  `voucher_id` bigint(20) NOT NULL COMMENT 'voucher id in campaigns.voucher table',
  `voucher_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'voucher code in camapaigns.voucher table',
  `type` enum('REFERRER','REFEREE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REFERRER',
  `identifier` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `voucher_series_id` bigint(20) NOT NULL,
  `event` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `till_id` int(20) NOT NULL,
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `voucher_id` (`voucher_id`),
  KEY `org_auto_update_time` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : referral_voucher_details
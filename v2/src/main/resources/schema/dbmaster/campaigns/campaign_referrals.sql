
-- start  Schema : campaign_referrals

CREATE TABLE `campaign_referrals` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL COMMENT 'store id where the referral was made',
  `voucher_series_id` bigint(20) NOT NULL,
  `referrer_id` bigint(20) NOT NULL COMMENT 'The id of the person making the referrals',
  `voucher_id` bigint(20) NOT NULL COMMENT 'id of the voucher sent to referrer',
  `referee_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `referee_mobile` varchar(13) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'mobile number of the referee',
  `referee_email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'email id of the referee',
  `voucher_redemption_id` int(11) DEFAULT NULL COMMENT 'id of the voucher redemption',
  `created_on` datetime NOT NULL,
  `last_modified` datetime DEFAULT NULL,
  `num_reminders` smallint(6) NOT NULL DEFAULT '0' COMMENT 'Number of times any reminders has been sent',
  `referee_id` bigint(20) DEFAULT NULL COMMENT 'user id of the referee',
  `last_reminded` datetime DEFAULT NULL COMMENT 'when was the last time a reminder was sent',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`voucher_series_id`,`referrer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Details about referrals on a campaign';


-- end  Schema : campaign_referrals
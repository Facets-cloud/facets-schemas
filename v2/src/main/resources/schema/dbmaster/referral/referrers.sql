
-- start  Schema : referrers

CREATE TABLE `referrers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_user_id` bigint(20) NOT NULL,
  `org_id` int(11) NOT NULL,
  `referral_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `campaign_id` bigint(20) NOT NULL,
  `token` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `token` (`token`),
  KEY `org_campaign_idx` (`org_id`,`campaign_id`),
  KEY `org_auto_update_time` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : referrers
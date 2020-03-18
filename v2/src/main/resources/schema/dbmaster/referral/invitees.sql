
-- start  Schema : invitees

CREATE TABLE `invitees` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `referrer_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `campaign_id` int(20) NOT NULL COMMENT 'The campaign id',
  `identifier` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'invitee identifier email/mobile',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` enum('MOBILE','EMAIL','SOCIAL') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'type of invitee',
  `client_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'the store code',
  `till_id` int(20) NOT NULL,
  `client_type` enum('CLIENT','MICRO_SITE','OTHER') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'the client type from where invitee came',
  `invited_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_campaign_idx` (`org_id`,`campaign_id`),
  KEY `org_auto_update_time` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : invitees
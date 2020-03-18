
-- start  Schema : campaign_meta

CREATE TABLE `campaign_meta` (
  `campaign_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `incentivise_type` enum('TRIGGER','FINAL') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'When to incentivise? Trigger based of report based(end of campaign)',
  `referral_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `default_at_pos` tinyint(1) NOT NULL COMMENT 'Is this campaign default at pos',
  `invite_loyalty` tinyint(1) NOT NULL DEFAULT '1',
  `base_url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sms_template` mediumtext COLLATE utf8mb4_unicode_ci,
  `email_subject` mediumtext COLLATE utf8mb4_unicode_ci,
  `email_template` mediumtext COLLATE utf8mb4_unicode_ci,
  `token` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  UNIQUE KEY `campaign_id` (`campaign_id`),
  KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : campaign_meta
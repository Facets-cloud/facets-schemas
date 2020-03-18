
-- start  Schema : subscription_rules

CREATE TABLE `subscription_rules` (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `type` enum('OPTIN','OPTOUT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPTOUT',
  `subscription_type` enum('SINGLE','DOUBLE','CONFIRMED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SINGLE',
  `channel_params_types` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Json encode two ids of emailParams and smsParams',
  `org_external_status` enum('NDNC','PDPA','NOT_SET') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NOT_SET',
  `last_updated_on` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  PRIMARY KEY (`rule_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=407 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : subscription_rules
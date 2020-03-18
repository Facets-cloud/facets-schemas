
-- start  Schema : subscription_rules_history

CREATE TABLE `subscription_rules_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subscription_rule_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `type` enum('OPTIN','OPTOUT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPTOUT',
  `subscription_type` enum('SINGLE','DOUBLE','CONFIRMED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SINGLE',
  `channel_params_types` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Json encode two ids of emailParams and smsParams',
  `last_updated_on` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=975 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : subscription_rules_history
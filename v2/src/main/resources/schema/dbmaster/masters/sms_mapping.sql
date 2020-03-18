
-- start  Schema : sms_mapping

CREATE TABLE `sms_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` enum('SMS','MISSED_CALL','BOTH') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shortcode` varchar(13) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `command` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_prefix_id` int(11) NOT NULL DEFAULT '-1' COMMENT 'Foreign Key From `incoming_interaction_org_prefix`; -1 = none',
  `org_id` bigint(20) NOT NULL,
  `action_id` bigint(20) NOT NULL,
  `notes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `whoami` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'when non null, should be supplied in the request',
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `params` mediumtext COLLATE utf8mb4_unicode_ci,
  `till_id` int(11) DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `UniqueShortcodeCommandActionidOrgprefixid` (`shortcode`,`command`,`action_id`,`org_prefix_id`),
  KEY `org_index` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : sms_mapping
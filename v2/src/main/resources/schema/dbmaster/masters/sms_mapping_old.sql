
-- start  Schema : sms_mapping_old

CREATE TABLE `sms_mapping_old` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` enum('SMS','MISSED_CALL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SMS',
  `shortcode` varchar(13) COLLATE utf8mb4_unicode_ci NOT NULL,
  `command` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `action_id` bigint(20) NOT NULL,
  `notes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `whoami` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'when non null, should be supplied in the request',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `shortcode` (`shortcode`,`command`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : sms_mapping_old

-- start  Schema : ftp_audience_upload

CREATE TABLE `ftp_audience_upload` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `group_name` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `folder` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `custom_tags` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('OPEN','COPYING','COPIED','PROCESSING','EXECUTED','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `confirm` tinyint(4) NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `custom_tag_count` int(11) NOT NULL DEFAULT '0',
  `import_type` enum('mobile','email','userid') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : ftp_audience_upload
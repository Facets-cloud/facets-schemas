
-- start  Schema : upload_files_history

CREATE TABLE `upload_files_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `params` mediumtext COLLATE utf8mb4_unicode_ci,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(10) DEFAULT NULL,
  `token` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` int(10) DEFAULT NULL,
  `group_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `upload_type` enum('sticky_group','campaign_users','test_group') COLLATE utf8mb4_unicode_ci DEFAULT 'campaign_users',
  `import_type` enum('mobile','email','userid','externalid') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `temp_table_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : upload_files_history
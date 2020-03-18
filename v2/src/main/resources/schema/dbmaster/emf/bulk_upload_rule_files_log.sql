
-- start  Schema : bulk_upload_rule_files_log

CREATE TABLE `bulk_upload_rule_files_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `file_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'file name of the bulk upload file',
  `rules_added` int(11) NOT NULL COMMENT 'number rules validated successfully and saved',
  `rules_failed` int(11) NOT NULL COMMENT 'number of rules failed in validation and are not saved',
  `uploaded_by` int(11) NOT NULL,
  `uploaded_on` datetime NOT NULL,
  `time_taken` int(11) NOT NULL COMMENT 'time taken in milliseconds to upload rules',
  `status` enum('INPROGRESS','SUCCESS','FAILED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'INPROGRESS',
  `error_message` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bulk_upload_rule_files_log
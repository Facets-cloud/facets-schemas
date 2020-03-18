
-- start  Schema : uploaded_files

CREATE TABLE `uploaded_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `file_tag` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_contents` longblob NOT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `extension` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mime_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `client_file_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'the file name which will be sent down to the client',
  `client_file_monitoring_type` enum('FILE_CHECK','PROCESS_CHECK') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'FILE_CHECK' COMMENT 'defines the kind of file check',
  `file_handle` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : uploaded_files
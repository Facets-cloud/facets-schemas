
-- start  Schema : subscription_import_files_history

CREATE TABLE `subscription_import_files_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `import_type` enum('PDPA','NDNC') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uploaded_file_handle` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_file_handle` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `imported_file_handle` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `temp_table_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uploaded_by` int(11) NOT NULL,
  `uploaded_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : subscription_import_files_history
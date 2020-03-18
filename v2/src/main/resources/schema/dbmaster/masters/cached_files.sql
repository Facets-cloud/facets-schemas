
-- start  Schema : cached_files

CREATE TABLE `cached_files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `action` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `file_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'key to be used for pulling the file out of s3',
  `file_extension` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'xml' COMMENT 'the extension of the file',
  `created_time` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `host` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_locked` tinyint(1) NOT NULL DEFAULT '0',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `is_hive_file` tinyint(1) NOT NULL DEFAULT '0',
  `store_id` int(11) NOT NULL DEFAULT '-1',
  `fs_handle` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_size` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_mod_act_idx` (`org_id`,`module`,`action`,`created_time`),
  KEY `store_id` (`store_id`,`org_id`,`module`,`action`,`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : cached_files

-- start  Schema : import_ftp_sources

CREATE TABLE `import_ftp_sources` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `server_url` varchar(99) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `base_folder` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `processed_folder` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sftp_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) DEFAULT '1',
  `non_stop` tinyint(1) NOT NULL DEFAULT '0',
  `email` text COLLATE utf8mb4_unicode_ci,
  `import_file_confs` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ref_id` int(11) NOT NULL DEFAULT '0' COMMENT 'ref_id is cron reference that is pointed to',
  `location_hash` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'for uniqueness of source location, replacement for UNIQUE KEY(org_id,server_url,username,base_folder)',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `location_hash` (`location_hash`),
  KEY `cron_reference` (`org_id`,`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=784 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : import_ftp_sources
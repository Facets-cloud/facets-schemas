
-- start  Schema : export_requests

CREATE TABLE `export_requests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_ref_id` int(11) NOT NULL,
  `request_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `source_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_ref_id` int(11) NOT NULL,
  `started_by` bigint(20) NOT NULL,
  `priority` enum('NORMAL','IMMEDIATE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL',
  `fields` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `filters` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_format` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `ftp_config` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `scp_config` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `mailing_list` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `export_mode` enum('FTP','EXTERNAL_S3') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'FTP',
  PRIMARY KEY (`id`),
  KEY `profile_idx` (`org_id`,`profile`),
  KEY `source_idx` (`org_id`,`source_type`,`source_ref_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=207099 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : export_requests
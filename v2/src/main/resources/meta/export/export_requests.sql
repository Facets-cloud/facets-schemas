
-- start  Schema : export_requests

CREATE TABLE `export_requests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `profile` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_ref_id` int(11) NOT NULL,
  `source_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_ref_id` int(11) NOT NULL,
  `priority` enum('NORMAL','IMMEDIATE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL',
  `fields` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `filters` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_format` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `ftp_config` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `scp_config` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `mailing_list` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `profile_idx` (`profile`,`org_id`),
  KEY `request_idx` (`request_ref_id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15935 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : export_requests
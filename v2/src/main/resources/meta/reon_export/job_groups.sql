
-- start  Schema : job_groups

CREATE TABLE `job_groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dump_type` enum('FTP','S3') COLLATE utf8mb4_unicode_ci NOT NULL,
  `ftp_tag` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_path` text COLLATE utf8mb4_unicode_ci,
  `recipients` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `source` enum('DATA_EXPORT','AUDIENCE_GROUP_MANAGER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DATA_EXPORT',
  `source_id` int(11) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `name` (`org_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20289 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : job_groups
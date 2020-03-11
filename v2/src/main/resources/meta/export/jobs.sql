
-- start  Schema : jobs

CREATE TABLE `jobs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_details` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_ref_id` int(11) NOT NULL,
  `started_by` bigint(11) NOT NULL,
  `status` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `file_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_service_handle` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_version` int(11) DEFAULT NULL,
  `error_message` mediumtext COLLATE utf8mb4_unicode_ci,
  `error_detail` longtext COLLATE utf8mb4_unicode_ci,
  `export_request_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `profile_idx` (`profile`,`org_id`),
  KEY `source_idx` (`source_type`,`source_ref_id`,`org_id`),
  KEY `status_idx` (`status`,`org_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : jobs
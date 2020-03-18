
-- start  Schema : clientlog_meta

CREATE TABLE `clientlog_meta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_handle` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `logged_time` datetime NOT NULL,
  `uploaded_time` datetime NOT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_signature` char(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SHA1 signature of file',
  `file_size` int(10) unsigned NOT NULL DEFAULT '0',
  `client_ip` bigint(20) DEFAULT NULL,
  `file_type` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT 'clieantlog',
  PRIMARY KEY (`id`,`org_id`),
  KEY `till_id` (`org_id`,`entity_id`),
  KEY `logged_time` (`entity_id`,`logged_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Metadata for log files uploaded by clients';


-- end  Schema : clientlog_meta
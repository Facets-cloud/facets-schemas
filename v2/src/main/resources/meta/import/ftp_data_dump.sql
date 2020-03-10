
-- start  Schema : ftp_data_dump

CREATE TABLE `ftp_data_dump` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `dump_call` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'dump call to identify the type/profile dump needed',
  `dump_file` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('INIT','STARTED','TEMP_DUMP_FILE_INPROGRESS','TEMP_DUMP_FILE_COMPLETE','FTP_PUT_INPROGRESS','FTP_PUT_COMPLETE','FAILED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `scheduler_params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `started_on` datetime NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `dump_call` (`dump_call`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : ftp_data_dump
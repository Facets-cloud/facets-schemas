
-- start  Schema : ftp_background_import

CREATE TABLE `ftp_background_import` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_id` int(11) NOT NULL,
  `org_id` int(10) NOT NULL DEFAULT '0',
  `tempdb_status` enum('TEMPDB_OPEN','TEMPDB_INPROGRESS','TEMPDB_DONE','TEMPDB_APPROVED','MAINDB_INPROGRESS','MAINDB_DONE','TEMPDB_FAILED','MAINDB_FAILED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` datetime NOT NULL,
  `added_by` int(11) NOT NULL,
  `last_picked_up` datetime NOT NULL,
  `tempdb_done_time` datetime NOT NULL,
  `maindb_done_time` datetime NOT NULL,
  `nsadmin_id_temp` bigint(20) DEFAULT '0',
  `nsadmin_id_main` bigint(20) DEFAULT '0',
  `priority` int(5) NOT NULL DEFAULT '50',
  `profile` int(1) NOT NULL DEFAULT '15' COMMENT 'default value is 15 which is not any profile_id. note this',
  `source_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `tempdb` (`tempdb_status`),
  KEY `file_id` (`file_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='table for background ftp synch';


-- end  Schema : ftp_background_import
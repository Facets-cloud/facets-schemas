
-- start  Schema : export_configs

CREATE TABLE `export_configs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `config_key` enum('FTP_SETUP','SCP_SETUP','EMAIL_RECEPIENTS','FILE_NAME_FORMAT','FILE_PASSWORD_PROTECTED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `ftp_tag` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'FTP',
  `config_value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` int(11) NOT NULL,
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_valid` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `org_config_key_idx` (`org_id`,`config_key`,`is_valid`)
) ENGINE=InnoDB AUTO_INCREMENT=2762 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : export_configs
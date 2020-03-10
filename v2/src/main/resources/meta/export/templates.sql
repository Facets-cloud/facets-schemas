
-- start  Schema : templates

CREATE TABLE `templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `profile` smallint(3) NOT NULL,
  `added_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `filters` longtext COLLATE utf8mb4_unicode_ci,
  `columns` mediumtext COLLATE utf8mb4_unicode_ci,
  `file_format` mediumtext COLLATE utf8mb4_unicode_ci,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `org_profile_idx` (`org_id`,`profile`),
  KEY `update_time_idx` (`org_id`,`last_updated_on`)
) ENGINE=InnoDB AUTO_INCREMENT=23936 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : templates
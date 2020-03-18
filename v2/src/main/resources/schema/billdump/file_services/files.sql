
-- start  Schema : files

CREATE TABLE `files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `handle` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `namespace` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_type` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `added_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `added_by_ip` bigint(20) NOT NULL,
  `last_update_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_accessed_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `additional_info` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Any additional info can be saved here',
  `acl` enum('PRIVATE','PUBLIC') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PRIVATE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`handle`),
  UNIQUE KEY `name_namespace` (`name`,`namespace`),
  KEY `namespace_id` (`namespace`)
) ENGINE=InnoDB AUTO_INCREMENT=16327109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : files
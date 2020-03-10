
-- start  Schema : actions

CREATE TABLE `actions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `namespace` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  `module_id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(4) NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `permission_id` int(11) NOT NULL,
  `visibility` tinyint(4) NOT NULL DEFAULT '1',
  `offline_supported` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Only those actions which supports SMS / MISSEDCALL etc',
  `is_dump` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'dump action are allowed only for very few users',
  `feature_id` int(11) NOT NULL DEFAULT '-1',
  `params` mediumtext COLLATE utf8mb4_unicode_ci,
  `offline_source` enum('SMS','MISSED_CALL','ALL') COLLATE utf8mb4_unicode_ci DEFAULT 'ALL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `module_id` (`module_id`,`code`,`name`,`namespace`,`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1731 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : actions
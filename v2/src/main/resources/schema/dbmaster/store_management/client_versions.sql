
-- start  Schema : client_versions

CREATE TABLE `client_versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_type` enum('INTOUCH','STORE_CENTER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `framework_version` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'V3.5',
  `change_log` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` datetime NOT NULL,
  `version_added_by` int(11) NOT NULL,
  `is_active` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Contains the versions of the clients we have';


-- end  Schema : client_versions
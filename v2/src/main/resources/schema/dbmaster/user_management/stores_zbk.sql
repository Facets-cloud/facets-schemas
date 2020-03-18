
-- start  Schema : stores_zbk

CREATE TABLE `stores_zbk` (
  `store_id` int(11) NOT NULL,
  `tag` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'org',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `firstname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `passwordhash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `secretq` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `secreta` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(13) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `org_id` bigint(20) NOT NULL DEFAULT '1',
  `is_inactive` tinyint(1) NOT NULL COMMENT 'Whether the user is active or not',
  `password_validity` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `replace_inactive_by` int(11) NOT NULL,
  PRIMARY KEY (`store_id`),
  KEY `username` (`username`),
  KEY `org_id` (`org_id`,`tag`),
  KEY `org_username_idx` (`org_id`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : stores_zbk
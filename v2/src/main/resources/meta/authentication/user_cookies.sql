
-- start  Schema : user_cookies

CREATE TABLE `user_cookies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `is_ip_session` tinyint(4) NOT NULL,
  `user_ip` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `cookie_hash` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `valid_days` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  `expires_on` timestamp NULL DEFAULT NULL,
  `status` enum('INVALID','TWO_FACTOR_PENDING','VALID','PENDING') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_hash_idx` (`ref_id`,`cookie_hash`),
  KEY `cookie_hash_idx` (`cookie_hash`)
) ENGINE=InnoDB AUTO_INCREMENT=35302798 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='SSO.';


-- end  Schema : user_cookies
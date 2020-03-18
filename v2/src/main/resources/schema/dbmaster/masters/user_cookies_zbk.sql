
-- start  Schema : user_cookies_zbk

CREATE TABLE `user_cookies_zbk` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `random_hash` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `created_on` datetime NOT NULL,
  `valid_till` datetime NOT NULL,
  `last_login` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `user_hash_idx` (`username`,`random_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_cookies_zbk
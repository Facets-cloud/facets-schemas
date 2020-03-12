
-- start  Schema : active_logins

CREATE TABLE `active_logins` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `is_valid` tinyint(3) NOT NULL,
  `user_agent` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `sso_cookie` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `user_ip` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `login_attempted_at` datetime NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `rohit` int,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ref_id` (`ref_id`,`sso_cookie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Maintains the active login for a user at any given time.';


-- end  Schema : active_logins
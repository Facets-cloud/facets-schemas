
-- start  Schema : user_login_history

CREATE TABLE `user_login_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `identifier` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `login_response` enum('SUCCESS','FAILURE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SUCCESS',
  `failed_reason` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SUCCESS',
  `login_attempted_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ref_id` (`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17849698 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Login history for each user.';


-- end  Schema : user_login_history

-- start  Schema : loggable_users_zbk

CREATE TABLE `loggable_users_zbk` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `type` enum('TILL','ADMIN_USER','STR_SERVER','ASSOCIATE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_id` int(11) NOT NULL,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `secret_question` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `secret_answer` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `password_validity` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `username_uidx` (`username`),
  UNIQUE KEY `ref_type_uidx` (`ref_id`,`type`),
  KEY `org_id` (`org_id`),
  KEY `org_id_2` (`org_id`,`ref_id`),
  KEY `user_name` (`username`),
  KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loggable_users_zbk
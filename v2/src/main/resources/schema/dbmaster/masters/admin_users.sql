
-- start  Schema : admin_users

CREATE TABLE `admin_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `reports_to` int(11) NOT NULL,
  `title` enum('Mr.','Mrs.','Miss.') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Mr.',
  `first_name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `middle_name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mobile_validated` tinyint(1) NOT NULL DEFAULT '0',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_validated` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `last_login` datetime NOT NULL,
  `created_on` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`mobile`),
  KEY `org_id_2` (`org_id`,`email`),
  KEY `org_id_3` (`org_id`,`reports_to`),
  KEY `time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=200132621 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : admin_users
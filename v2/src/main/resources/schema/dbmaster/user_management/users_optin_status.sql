
-- start  Schema : users_optin_status

CREATE TABLE `users_optin_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ndnc_status_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `org_id` int(11) NOT NULL,
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` datetime NOT NULL,
  `last_updated` date NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `ndnc_status_id` (`ndnc_status_id`),
  UNIQUE KEY `org_id` (`org_id`,`mobile`),
  KEY `last_updated` (`last_updated`,`org_id`),
  KEY `org_user_idx` (`org_id`,`user_id`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : users_optin_status
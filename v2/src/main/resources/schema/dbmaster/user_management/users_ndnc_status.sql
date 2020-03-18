
-- start  Schema : users_ndnc_status

CREATE TABLE `users_ndnc_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `org_id` int(11) NOT NULL,
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('NDNC','INVALID','DND','UNKNOWN') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_mobile` (`org_id`,`mobile`),
  UNIQUE KEY `user_org_mobile` (`user_id`,`org_id`,`mobile`),
  UNIQUE KEY `user_id` (`user_id`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE,
  KEY `user_id_status` (`user_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : users_ndnc_status
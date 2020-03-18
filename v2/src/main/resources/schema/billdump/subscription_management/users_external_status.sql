
-- start  Schema : users_external_status

CREATE TABLE `users_external_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `target_value` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_type` enum('SMS','EMAIL','WECHAT','ANDROID','IOS') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SMS',
  `scope_id` int(11) NOT NULL DEFAULT '1',
  `added_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `account_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '-1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `external_idx` (`target_value`,`target_type`,`account_id`,`scope_id`),
  KEY `auto_update_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=107076915 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : users_external_status
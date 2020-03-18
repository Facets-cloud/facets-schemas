
-- start  Schema : users

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ind',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `firstname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `passwordhash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `secretq` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `secreta` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(25) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `org_id` bigint(20) NOT NULL DEFAULT '1',
  `is_inactive` tinyint(1) NOT NULL COMMENT 'Whether the user is active or not',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source` enum('instore','e-comm','newsletter','campaigns','NCA','WECHAT','MARTJACK','WEB_ENGAGE','FACEBOOK','TMALL','OTHERS','WEBSITE','TAOBAO','JD','ECOMMERCE','KAOLA','PINDUODUO','SUNING','GLOBAL_SCANNER','XIAOHONGSHU') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'instore',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `mobile_2` (`org_id`,`mobile`) USING BTREE,
  UNIQUE KEY `email_2` (`org_id`,`email`) USING BTREE,
  KEY `username` (`username`),
  KEY `org_id` (`org_id`,`tag`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : users
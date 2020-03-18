
-- start  Schema : request_settings

CREATE TABLE `request_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('CHANGE_IDENTIFIER','GOODWILL','OTHERS') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `base_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `default_value` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : request_settings

-- start  Schema : supported_languages

CREATE TABLE `supported_languages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) NOT NULL,
  `language` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `iso_code` varchar(3) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'iso_code',
  PRIMARY KEY (`id`),
  KEY `time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : supported_languages
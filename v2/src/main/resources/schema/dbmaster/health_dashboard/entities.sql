
-- start  Schema : entities

CREATE TABLE `entities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entity` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_settings` tinyint(4) NOT NULL DEFAULT '1',
  `valid_till_days` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : entities
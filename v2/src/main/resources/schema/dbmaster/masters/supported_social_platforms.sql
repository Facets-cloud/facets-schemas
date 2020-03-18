
-- start  Schema : supported_social_platforms

CREATE TABLE `supported_social_platforms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `logo_url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : supported_social_platforms
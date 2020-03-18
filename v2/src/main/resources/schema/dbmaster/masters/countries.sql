
-- start  Schema : countries

CREATE TABLE `countries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `capital` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'country code represented by 2 digits',
  `short_name` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '3 digit country code',
  `numeric_code` int(11) NOT NULL COMMENT 'countries numeric code',
  `iso_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'countries ISO name',
  `mobile_country_code` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile_regex` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile_length_csv` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_by` int(11) NOT NULL DEFAULT '0',
  `last_updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=848 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : countries
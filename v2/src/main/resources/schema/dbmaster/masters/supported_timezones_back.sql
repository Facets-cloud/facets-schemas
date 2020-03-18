
-- start  Schema : supported_timezones_back

CREATE TABLE `supported_timezones_back` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) NOT NULL,
  `coordinates` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `timezone` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comments` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `std_offset` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `summer_offset` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : supported_timezones_back
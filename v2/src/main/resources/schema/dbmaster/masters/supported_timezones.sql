
-- start  Schema : supported_timezones

CREATE TABLE `supported_timezones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) NOT NULL,
  `coordinates` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `timezone` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comments` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `std_offset` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `summer_offset` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `timezone_offset` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ISO Timezone representation ',
  PRIMARY KEY (`id`),
  KEY `time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=406 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : supported_timezones
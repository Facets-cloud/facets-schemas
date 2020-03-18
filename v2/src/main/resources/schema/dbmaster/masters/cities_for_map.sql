
-- start  Schema : cities_for_map

CREATE TABLE `cities_for_map` (
  `id` int(11) NOT NULL,
  `country_id` int(11) NOT NULL,
  `actual_country_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `actual_state_id` int(11) NOT NULL,
  `city_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `time_zone_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `country_id` (`country_id`),
  KEY `actual_country_id` (`actual_country_id`),
  KEY `state_id` (`state_id`),
  KEY `actual_state_id` (`actual_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : cities_for_map
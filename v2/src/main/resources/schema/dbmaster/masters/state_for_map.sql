
-- start  Schema : state_for_map

CREATE TABLE `state_for_map` (
  `id` int(11) NOT NULL,
  `actual_state_id` int(11) NOT NULL,
  `country_id` int(11) NOT NULL,
  `actual_country_id` int(11) NOT NULL,
  `state_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `actual_state_id` (`actual_state_id`),
  KEY `country_id` (`country_id`),
  KEY `actual_country_id` (`actual_country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : state_for_map
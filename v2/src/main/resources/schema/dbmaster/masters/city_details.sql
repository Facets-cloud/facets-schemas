
-- start  Schema : city_details

CREATE TABLE `city_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `city_name` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `time_zone_id` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `country_id` (`country_id`,`state_id`,`city_name`),
  KEY `time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=37558 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : city_details
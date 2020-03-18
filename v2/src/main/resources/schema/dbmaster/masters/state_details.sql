
-- start  Schema : state_details

CREATE TABLE `state_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) NOT NULL,
  `state_name` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `country_id_2` (`country_id`,`state_name`),
  KEY `country_id` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5532 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : state_details
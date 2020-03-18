
-- start  Schema : simple_property_info

CREATE TABLE `simple_property_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_info_id` int(11) DEFAULT NULL,
  `property_key` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `property_value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `initialized` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `property_id` (`action_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1616 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : simple_property_info
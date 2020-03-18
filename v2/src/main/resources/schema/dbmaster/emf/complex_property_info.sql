
-- start  Schema : complex_property_info

CREATE TABLE `complex_property_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `property_id` int(11) DEFAULT NULL,
  `property_key` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `property_value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'It will be a simple property id',
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : complex_property_info
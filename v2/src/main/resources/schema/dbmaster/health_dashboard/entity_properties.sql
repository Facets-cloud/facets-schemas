
-- start  Schema : entity_properties

CREATE TABLE `entity_properties` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `entity_id` int(20) NOT NULL,
  `property_id` int(20) NOT NULL,
  `track` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : entity_properties
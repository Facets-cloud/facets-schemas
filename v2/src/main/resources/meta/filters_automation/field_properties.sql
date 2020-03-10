
-- start  Schema : field_properties

CREATE TABLE `field_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `form_part_id` int(11) NOT NULL,
  `field_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_value` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_mapping_field` tinyint(1) NOT NULL DEFAULT '0',
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `form_id_field_type` (`form_part_id`,`field_type`)
) ENGINE=InnoDB AUTO_INCREMENT=742 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : field_properties
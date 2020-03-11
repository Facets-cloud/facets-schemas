
-- start  Schema : form_parts

CREATE TABLE `form_parts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filter_id` int(11) NOT NULL,
  `base_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_collection` tinyint(1) NOT NULL DEFAULT '0',
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : form_parts
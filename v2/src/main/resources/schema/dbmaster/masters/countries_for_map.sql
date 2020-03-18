
-- start  Schema : countries_for_map

CREATE TABLE `countries_for_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actual_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `short_name` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numeric_code` int(11) NOT NULL,
  `capital` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : countries_for_map
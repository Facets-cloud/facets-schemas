
-- start  Schema : points_source_types

CREATE TABLE `points_source_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'auto generated point source type id',
  `type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '(eg: Bill / LineItem / Birthday / Promotion etc)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_source_types
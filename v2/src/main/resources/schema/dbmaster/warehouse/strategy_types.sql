
-- start  Schema : strategy_types

CREATE TABLE `strategy_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'unique id of the strategy',
  `type` enum('POINT_ALLOCATION','POINT_EXPIRY','POINT_REDEMPTION_THRESHOLD','SLAB_UPGRADE','SLAB_DOWNGRADE','POINT_RETURN','EXPIRY_REMINDER','TRACKER') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'description of the strategy',
  `class_name` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'name of the strategy class to be loaded',
  `property_template` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'json template exposing the properties that needs to be configured',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : strategy_types
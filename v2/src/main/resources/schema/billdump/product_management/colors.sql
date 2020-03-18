
-- start  Schema : colors

CREATE TABLE `colors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pallette` int(11) unsigned NOT NULL COMMENT 'pallette defning color as rgb',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pallette_idx` (`pallette`)
) ENGINE=InnoDB AUTO_INCREMENT=424 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Colors meta table';


-- end  Schema : colors
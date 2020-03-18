
-- start  Schema : org_colors

CREATE TABLE `org_colors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `hexpallette` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pallette` int(11) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hex_pallette_idx` (`org_id`,`hexpallette`),
  UNIQUE KEY `pallette_idx` (`org_id`,`pallette`),
  KEY `auto_time_idx` (`auto_update_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=526191 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Colors org meta table';


-- end  Schema : org_colors

-- start  Schema : export_profile_fields

CREATE TABLE `export_profile_fields` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `export_profile_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `datatype` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STRING',
  `datasize` smallint(6) DEFAULT NULL,
  `label` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_mandatory` tinyint(1) NOT NULL DEFAULT '0',
  `is_internal` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `export_profile_id` (`export_profile_id`)
) ENGINE=InnoDB AUTO_INCREMENT=288 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : export_profile_fields
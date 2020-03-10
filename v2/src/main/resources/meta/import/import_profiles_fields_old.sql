
-- start  Schema : import_profiles_fields_old

CREATE TABLE `import_profiles_fields_old` (
  `profile_id` int(11) NOT NULL,
  `field_id` int(11) NOT NULL,
  `field_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_data_type` enum('INT','VARCHAR','DATETIME','TEXT','FLOAT','VARBINARY','DOUBLE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `field_length` int(11) NOT NULL,
  `field_validation_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_mandatory` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`profile_id`,`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_profiles_fields_old
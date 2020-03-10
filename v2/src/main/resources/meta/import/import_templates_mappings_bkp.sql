
-- start  Schema : import_templates_mappings_bkp

CREATE TABLE `import_templates_mappings_bkp` (
  `template_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `field_id` int(11) NOT NULL,
  `field_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_type` enum('BASE','CUSTOM') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'BASE',
  `field_index` int(11) NOT NULL,
  `field_not_null` int(1) NOT NULL,
  `field_transformer` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `transformer_inputs` mediumtext COLLATE utf8mb4_unicode_ci,
  `field_validation_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `field_data_type` enum('VARCHAR','DATETIME','INT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_length` int(11) NOT NULL,
  PRIMARY KEY (`template_id`,`field_id`,`ref_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_templates_mappings_bkp
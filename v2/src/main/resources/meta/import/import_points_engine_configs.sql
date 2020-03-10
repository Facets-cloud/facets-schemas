
-- start  Schema : import_points_engine_configs

CREATE TABLE `import_points_engine_configs` (
  `org_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `pe_program_details` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`template_id`),
  KEY `template_idx` (`org_id`,`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_points_engine_configs
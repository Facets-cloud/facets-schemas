
-- start  Schema : bill_dump_import_attributes

CREATE TABLE `bill_dump_import_attributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `extraction_type` enum('UPLOAD','POS','REGEX','USERDEF') COLLATE utf8mb4_unicode_ci NOT NULL,
  `extraction_params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_type` enum('String','Int','Boolean','Double') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'String',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bill_dump_import_attributes
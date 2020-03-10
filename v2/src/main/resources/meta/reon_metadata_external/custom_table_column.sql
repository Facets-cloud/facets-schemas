
-- start  Schema : custom_table_column

CREATE TABLE `custom_table_column` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_id` int(11) NOT NULL,
  `custom_table_id` int(11) NOT NULL,
  `column_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `display_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `transpose_type` enum('GROUPING','MAP','TRANSPOSE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TRANSPOSE',
  `column_type` enum('ATTRIBUTE','MEASURE','PK') COLLATE utf8mb4_unicode_ci NOT NULL,
  `usability_type` enum('DUMP_ONLY','PARTIAL_KEY','SELECT','FILTER','GROUP','BANDING') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DUMP_ONLY',
  `ordinal_position` int(11) NOT NULL DEFAULT '1',
  `data_type` enum('TEXT','NUMBER','BIGNUMBER','BOOLEAN','DATE','TIME') COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `is_attr_table_present` tinyint(1) NOT NULL DEFAULT '0',
  `attr_table_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_data_synced` tinyint(1) NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `last_synced_on` timestamp NULL DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_cols` (`custom_table_id`,`asset_id`,`scope_id`),
  KEY `scope_is_synced` (`scope_id`,`is_data_synced`)
) ENGINE=InnoDB AUTO_INCREMENT=38968 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : custom_table_column
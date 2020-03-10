
-- start  Schema : external_dimension_columns

CREATE TABLE `external_dimension_columns` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dim_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `column_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `column_type` enum('PK','VALUE','ATTRIBUTE','DATE_ATTRIBUTE','TIME_ATTRIBUTE','ALIAS','NONE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `version` bigint(20) NOT NULL,
  `data_type` enum('TEXT','NUMBER','BIGNUMBER','BOOLEAN','DATE','TIME') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TEXT',
  `display_name` text COLLATE utf8mb4_unicode_ci,
  `usability_type` enum('DUMP_ONLY','PARTIAL_KEY','SELECT','FILTER','GROUP','BANDING') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SELECT',
  `functional_dependency_column` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dim_table_id_column_name` (`dim_id`,`column_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : external_dimension_columns
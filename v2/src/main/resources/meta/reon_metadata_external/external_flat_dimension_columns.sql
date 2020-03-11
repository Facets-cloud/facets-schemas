
-- start  Schema : external_flat_dimension_columns

CREATE TABLE `external_flat_dimension_columns` (
  `column_id` int(11) NOT NULL AUTO_INCREMENT,
  `dim_table_id` int(11) NOT NULL,
  `column_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `column_type` enum('PK','VALUE','ATTRIBUTE','DATE_ATTRIBUTE','TIME_ATTRIBUTE','ALIAS','NONE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `scope_id` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `data_type` enum('TEXT','NUMBER','BIGNUMBER','BOOLEAN','DATE','TIME') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TEXT',
  `value_col_scope` enum('CAP','ORG') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ORG',
  `display_name` text COLLATE utf8mb4_unicode_ci,
  `usability_type` enum('DUMP_ONLY','PARTIAL_KEY','SELECT','FILTER','GROUP') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SELECT',
  `functional_dependency_column` int(11) DEFAULT NULL,
  `unique_value_dependent_column` int(11) DEFAULT NULL,
  `attr_table_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_attr_value_table_present` tinyint(1) NOT NULL DEFAULT '0',
  `computation_type` enum('ETL','VIEW') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ETL',
  `location_table_type` enum('FACT','DIMENSION','VIEW','ATTR') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ATTR',
  `location_type_entity_id` int(11) DEFAULT NULL,
  `scd_type` enum('NONE','SOURCE_GENERATED','ETL_GENERATED','VERSIONED_PARTITIONS') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `scd_table_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`column_id`),
  UNIQUE KEY `dim_table_id_column_name` (`dim_table_id`,`scope_id`,`column_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : external_flat_dimension_columns
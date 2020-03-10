
-- start  Schema : dimension_groups

CREATE TABLE `dimension_groups` (
  `column_id` int(11) NOT NULL AUTO_INCREMENT,
  `dim_table_id` int(11) NOT NULL,
  `dim_id` int(11) NOT NULL,
  `dimension_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_attribute` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_attribute_type` enum('HIERARCHICAL','FLAT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'FLAT',
  `parent_attribute_column_id` int(11) NOT NULL,
  `column_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `display_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `column_type` enum('PK','VALUE','LEVEL','DATE_ATTRIBUTE','TIME_ATTRIBUTE','ATTRIBUTE','ALIAS','NONE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `level_num` int(11) NOT NULL,
  `level_num_ref` int(11) DEFAULT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `data_type` enum('TEXT','NUMBER','BIGNUMBER','BOOLEAN','DATE','TIME') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TEXT',
  `value_col_scope` enum('CAP','ORG') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ORG',
  `usability_type` enum('SELECT','FILTER','GROUP') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SELECT',
  `parent_functional_dependency_column` int(11) NOT NULL DEFAULT '0',
  `computation_type` enum('ETL','VIEW','WORKFLOW') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'WORKFLOW',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`column_id`),
  UNIQUE KEY `dim_table_id_column_name` (`dim_table_id`,`parent_attribute`,`column_name`,`scope_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : dimension_groups
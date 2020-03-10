
-- start  Schema : external_dimension_source_column_mapping

CREATE TABLE `external_dimension_source_column_mapping` (
  `id` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fact_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dimension_column_link` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_column_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `version` bigint(20) NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_joining_required` tinyint(1) NOT NULL,
  `dim_table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `join_col_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `select_col_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `formatter_info` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : external_dimension_source_column_mapping
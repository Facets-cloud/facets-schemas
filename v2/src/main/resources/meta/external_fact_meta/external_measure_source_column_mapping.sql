
-- start  Schema : external_measure_source_column_mapping

CREATE TABLE `external_measure_source_column_mapping` (
  `id` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fact_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fact_column_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_column_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `version` bigint(20) NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mapping_target_table` (`fact_column_id`,`source_column_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : external_measure_source_column_mapping
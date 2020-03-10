
-- start  Schema : external_fact_data_location

CREATE TABLE `external_fact_data_location` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fact_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `run_id` int(11) NOT NULL DEFAULT '-1000',
  `s3_path` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_table_schema` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`fact_name`,`run_id`,`scope_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : external_fact_data_location
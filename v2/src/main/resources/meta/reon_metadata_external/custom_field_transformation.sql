
-- start  Schema : custom_field_transformation

CREATE TABLE `custom_field_transformation` (
  `custom_table_id` int(11) NOT NULL,
  `meta_src_table_id` int(11) NOT NULL,
  `meta_source_table_filter_expr` text COLLATE utf8mb4_unicode_ci,
  `meta_src_join_tables` text COLLATE utf8mb4_unicode_ci,
  `column_select_col_id` int(11) NOT NULL,
  `column_name_col_id` int(11) NOT NULL,
  `column_data_type_col_id` int(11) DEFAULT NULL,
  `data_src_table_id` int(11) NOT NULL,
  `data_src_table_filter_expr` text COLLATE utf8mb4_unicode_ci,
  `data_src_join_tables` text COLLATE utf8mb4_unicode_ci,
  `data_src_table_primay_dim_src_fk` int(11) NOT NULL,
  `data_src_table_instance_id` int(11) NOT NULL,
  `group_by_col` int(11) NOT NULL,
  `value_col_id` int(11) NOT NULL,
  `condition_col_id` int(11) NOT NULL,
  `is_condition_col_id_based` tinyint(1) NOT NULL DEFAULT '1',
  `is_disabled_src_col_id` int(11) DEFAULT NULL,
  `active_status` enum('ACTIVE','DISABLE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `added_on` timestamp NULL DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_scope_filter_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `scope_filter_meta_info` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`custom_table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : custom_field_transformation

-- start  Schema : custom_table

CREATE TABLE `custom_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `table_type` enum('CUSTOM_FIELDS','DIM_ATTR','FACT_MEASURE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CUSTOM_FIELDS',
  `sync_type` enum('ETL','EXTERNAL_SRC') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'EXTERNAL_SRC',
  `column_prefix` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'external',
  `provider_table_id` int(11) NOT NULL,
  `linked_table_id` int(11) NOT NULL,
  `is_attr_table_required` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `is_linked_table` tinyint(1) NOT NULL DEFAULT '0',
  `added_on` timestamp NULL DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : custom_table
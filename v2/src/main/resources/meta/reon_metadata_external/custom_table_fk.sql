
-- start  Schema : custom_table_fk

CREATE TABLE `custom_table_fk` (
  `custom_table_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `link_table_type` enum('DIM','FACT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `link_table_id` int(11) NOT NULL,
  `link_table_cols` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `link_table_filter` text COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `added_on` timestamp NULL DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`custom_table_id`,`link_table_id`,`link_table_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : custom_table_fk
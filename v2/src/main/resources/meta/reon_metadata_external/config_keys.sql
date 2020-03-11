
-- start  Schema : config_keys

CREATE TABLE `config_keys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `join_tables` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `default_value` int(11) NOT NULL,
  `is_valid` tinyint(4) NOT NULL,
  `value_src_table_id` int(11) NOT NULL,
  `value_select_col_id` int(11) NOT NULL,
  `filters` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : config_keys
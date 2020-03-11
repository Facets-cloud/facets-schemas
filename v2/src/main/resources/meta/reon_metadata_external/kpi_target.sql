
-- start  Schema : kpi_target

CREATE TABLE `kpi_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `org_id` int(11) NOT NULL DEFAULT '-1',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `kpis` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_by_dimensions` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `added_on` timestamp NULL DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `db_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_target_set` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `target_kpis` (`org_id`,`kpis`,`group_by_dimensions`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : kpi_target

-- start  Schema : kpi_merged_data_location

CREATE TABLE `kpi_merged_data_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kpi_id` int(11) NOT NULL,
  `group_by_dimensions` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `location` tinytext COLLATE utf8mb4_unicode_ci,
  `org_id` int(11) NOT NULL DEFAULT '-1',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kpi_group_dim` (`kpi_id`,`group_by_dimensions`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : kpi_merged_data_location
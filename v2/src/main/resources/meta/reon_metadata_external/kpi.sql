
-- start  Schema : kpi

CREATE TABLE `kpi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kpi_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `added_on` timestamp NULL DEFAULT NULL,
  `is_additive` tinyint(1) NOT NULL DEFAULT '0',
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kpi` (`kpi_id`,`scope_id`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : kpi
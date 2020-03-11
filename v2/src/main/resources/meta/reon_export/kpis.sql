
-- start  Schema : kpis

CREATE TABLE `kpis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `kpi_ref_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `template_id` bigint(20) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `name` (`org_id`,`template_id`,`kpi_ref_id`),
  KEY `template_id` (`org_id`,`template_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : kpis
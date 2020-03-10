
-- start  Schema : template_filters

CREATE TABLE `template_filters` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `template_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dimension_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `attribute_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operator` enum('EQUALS','LESS','IN','GREATER_THAN','LESS_THAN','GREATER_THAN_EQUALS','LESS_THAN_EQUALS','NOT_EQUALS','NOT_IN','BETWEEN','IS','IS_NOT','LIKE','ANNUAL_DAY') COLLATE utf8mb4_unicode_ci NOT NULL,
  `fields` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `name` (`template_name`,`dimension_name`,`attribute_name`),
  KEY `job_template` (`org_id`,`template_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : template_filters
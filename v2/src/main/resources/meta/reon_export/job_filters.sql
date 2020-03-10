
-- start  Schema : job_filters

CREATE TABLE `job_filters` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `dimension_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `attribute_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operator` enum('EQUALS','LESS','IN','GREATER_THAN','LESS_THAN','GREATER_THAN_EQUALS','LESS_THAN_EQUALS','NOT_EQUALS','NOT_IN','BETWEEN','IS','IS_NOT','LIKE','ANNUAL_DAY') COLLATE utf8mb4_unicode_ci NOT NULL,
  `fields` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `name` (`id`,`dimension_name`,`attribute_name`) USING BTREE,
  KEY `job_template` (`org_id`,`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6761 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : job_filters
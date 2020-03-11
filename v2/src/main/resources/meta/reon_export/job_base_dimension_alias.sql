
-- start  Schema : job_base_dimension_alias

CREATE TABLE `job_base_dimension_alias` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `dim_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `attr_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `alias_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : job_base_dimension_alias
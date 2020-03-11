
-- start  Schema : jobs

CREATE TABLE `jobs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `job_group_id` bigint(20) NOT NULL,
  `template_id` bigint(20) NOT NULL,
  `customers_group_id` bigint(20) NOT NULL,
  `customers_group_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `alias` text COLLATE utf8mb4_unicode_ci,
  `primary_date_column` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primary_time_column` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ftp_folder_path` text COLLATE utf8mb4_unicode_ci,
  `created_by` bigint(20) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `job` (`org_id`,`job_group_id`,`template_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : jobs
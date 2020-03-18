
-- start  Schema : segment_error_summary

CREATE TABLE `segment_error_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `segment_id` bigint(20) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `error_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_description` text COLLATE utf8mb4_unicode_ci,
  `count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_error_summary

-- start  Schema : changelog

CREATE TABLE `changelog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `segment_id` bigint(20) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : changelog
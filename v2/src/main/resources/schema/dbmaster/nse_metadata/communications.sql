
-- start  Schema : communications

CREATE TABLE `communications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `segment_id` bigint(20) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `nsadmin_id` bigint(20) DEFAULT NULL,
  `type` enum('EMAIL','SMS') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `receiver_info` text COLLATE utf8mb4_unicode_ci,
  `description` text COLLATE utf8mb4_unicode_ci,
  `status` enum('SUCCESS','ERROR') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : communications
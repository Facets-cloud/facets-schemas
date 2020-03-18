
-- start  Schema : events

CREATE TABLE `events` (
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL,
  `type` enum('VALIDATE_LOYALTY','VALIDATE_SEG_VAL','COMMUNICATION','OLTP_CACHING','DIM_TRIGGER') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('OPEN','IN_PROGRESS','SUCCESS','ERROR') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_code` int(11) DEFAULT NULL,
  `error_description` text COLLATE utf8mb4_unicode_ci,
  `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : events
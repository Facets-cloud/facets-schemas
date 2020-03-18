
-- start  Schema : segment_data_location

CREATE TABLE `segment_data_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) DEFAULT NULL,
  `segment_id` bigint(20) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `external_source` text COLLATE utf8mb4_unicode_ci,
  `success_source` text COLLATE utf8mb4_unicode_ci,
  `error_source` text COLLATE utf8mb4_unicode_ci,
  `valid_on` datetime DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_id` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_segment` (`org_id`,`segment_id`,`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_data_location
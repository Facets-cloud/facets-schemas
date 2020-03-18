
-- start  Schema : segment_job_details

CREATE TABLE `segment_job_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `segment_id` bigint(20) NOT NULL,
  `source_params` text COLLATE utf8mb4_unicode_ci,
  `stats_params` text COLLATE utf8mb4_unicode_ci,
  `status` enum('PROCESSING','SUCCESS','FAILED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_by` bigint(20) DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `segment_index` (`org_id`,`segment_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_job_details

-- start  Schema : calculate_kpi_job

CREATE TABLE `calculate_kpi_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL COMMENT 'ID of the organization for which gap is to be calculated',
  `program_id` int(11) NOT NULL COMMENT 'ID of the program for which gap is to be calculated',
  `kpi_version` int(11) NOT NULL COMMENT 'New version which is to be associated with all calculated values',
  `gap_calculation_mode` enum('GAP_TO_UPGRADE','GAP_TO_RENEW') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'GAP_TO_UPGRADE: Calculate only gap to upgrade.\nGAP_TO_RENEW: Calculate only gap to renew.',
  `status` enum('TO_BE_PROCESSED','PROCESSING','SUCCESS','FAILED','CANCELLED') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'TO_BE_PROCESSED: Jobs to be picked for processing. PROCESSING: Jobs currently processing. FAILED: Job failed to process. SUCCESS: Job completed successfully. CANCELLED: Job cancelled because new job is scheduled by user.',
  `processed_upto_id` int(11) NOT NULL COMMENT 'Customer id in the program for which calculation is completed',
  `ending_id` int(11) NOT NULL COMMENT 'Maximum customer of the program till which calculation has to be done',
  `job_creation_date` datetime NOT NULL COMMENT 'Date when job for the program is created',
  `job_start_date` datetime DEFAULT NULL COMMENT 'Date when job is started',
  `job_scheduled_date` datetime NOT NULL COMMENT 'Date when job is scheduled to run',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `program_id` (`org_id`,`program_id`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : calculate_kpi_job
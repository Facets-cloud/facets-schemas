
-- start  Schema : cron

CREATE TABLE `cron` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `job_group_id` bigint(20) NOT NULL,
  `pattern` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_type` enum('DAILY','WEEKLY','MONTHLY','ONE_TIME') COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_value` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_to_select` enum('LD','LM','LW','ONE_TIME') COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_period` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_validity` date NOT NULL,
  `end_validity` date NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `real_time_schedule` tinyint(1) NOT NULL DEFAULT '0',
  `created_by` bigint(20) NOT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `name` (`org_id`,`job_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18908 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : cron
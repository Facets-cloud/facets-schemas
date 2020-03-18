
-- start  Schema : target_periods

CREATE TABLE `target_periods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `target_group_id` bigint(20) NOT NULL,
  `period_ref_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `end_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created_on` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NULL DEFAULT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `unique_period_code` (`org_id`,`target_group_id`,`period_ref_code`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_target_group` (`target_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : target_periods
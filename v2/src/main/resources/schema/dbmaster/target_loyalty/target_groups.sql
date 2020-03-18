
-- start  Schema : target_groups

CREATE TABLE `target_groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NULL DEFAULT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `preferred_till_id` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `unique_name` (`org_id`,`name`),
  KEY `auto_time_idx` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : target_groups
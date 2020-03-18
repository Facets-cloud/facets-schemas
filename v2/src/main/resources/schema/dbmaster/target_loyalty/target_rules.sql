
-- start  Schema : target_rules

CREATE TABLE `target_rules` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `target_group_id` bigint(20) NOT NULL,
  `target_type` enum('QUANTITY','SALES') COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_entity` enum('TRANSACTION','POINTS','LINEITEM') COLLATE utf8mb4_unicode_ci NOT NULL,
  `emf_ruleset_id` bigint(20) DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NULL DEFAULT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `emf_payload` mediumtext COLLATE utf8mb4_unicode_ci,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `emf_unrolled_ruleset_id` bigint(20) DEFAULT NULL,
  `emf_unrolled_payload` mediumtext COLLATE utf8mb4_unicode_ci,
  `expression` mediumtext COLLATE utf8mb4_unicode_ci,
  `expression_json` mediumtext COLLATE utf8mb4_unicode_ci,
  `filters_json` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `unique_rule_name` (`org_id`,`name`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_target_group` (`target_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : target_rules
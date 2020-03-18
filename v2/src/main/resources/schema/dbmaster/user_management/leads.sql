
-- start  Schema : leads

CREATE TABLE `leads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `type` enum('SKU','CATEGORY','BRAND','CUSTOM') COLLATE utf8mb4_unicode_ci DEFAULT 'SKU',
  `lead_for` mediumtext COLLATE utf8mb4_unicode_ci,
  `created_date` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `updated_by` bigint(20) NOT NULL,
  `status` enum('OPEN','WON','LOST','ON_HOLD','DELETED') COLLATE utf8mb4_unicode_ci DEFAULT 'OPEN',
  `sub_status_id` int(11) DEFAULT NULL,
  `last_follow_up` timestamp NULL DEFAULT NULL,
  `next_follow_up` timestamp NULL DEFAULT NULL,
  `owner` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `org_source_id` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_user` (`org_id`,`user_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : leads
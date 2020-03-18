
-- start  Schema : leads_sub_status

CREATE TABLE `leads_sub_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `status` enum('OPEN','WON','LOST','ON_HOLD','DELETED') COLLATE utf8mb4_unicode_ci DEFAULT 'OPEN',
  `sub_status` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_status` (`org_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : leads_sub_status
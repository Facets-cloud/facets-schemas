
-- start  Schema : lead_status_log

CREATE TABLE `lead_status_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `lead_id` bigint(20) NOT NULL,
  `status` enum('OPEN','WON','LOST','ON_HOLD','DELETED') COLLATE utf8mb4_unicode_ci DEFAULT 'OPEN',
  `sub_status_id` int(11) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `reason_id` int(11) DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_user` (`org_id`,`user_id`,`status`),
  KEY `idx_lead` (`org_id`,`lead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : lead_status_log

-- start  Schema : lead_follow_up

CREATE TABLE `lead_follow_up` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `lead_id` bigint(20) NOT NULL,
  `scheduled_follow_up` timestamp NULL DEFAULT NULL,
  `followed_up_on` timestamp NULL DEFAULT NULL,
  `followed_up_by` bigint(20) NOT NULL,
  `next_follow_up` timestamp NULL DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_user` (`org_id`,`user_id`),
  KEY `idx_lead` (`org_id`,`lead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : lead_follow_up

-- start  Schema : user_group_pending_requests

CREATE TABLE `user_group_pending_requests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `group_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `status` enum('OPEN','ACCEPTED','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'OPEN',
  `requested_on` timestamp NULL DEFAULT NULL,
  `requested_by` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_group` (`org_id`,`group_id`),
  KEY `idx_user` (`org_id`,`user_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : user_group_pending_requests
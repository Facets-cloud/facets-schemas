
-- start  Schema : target_communications

CREATE TABLE `target_communications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `target_group_id` bigint(20) NOT NULL,
  `communication_type` enum('START','FINISH','CHANGE','REMINDER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `channel_id` int(11) NOT NULL,
  `reminder_offset_days` int(11) DEFAULT NULL,
  `subject_template` text COLLATE utf8mb4_unicode_ci,
  `message_template` text COLLATE utf8mb4_unicode_ci,
  `sender` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `created_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `last_updated_by` bigint(20) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_user` (`org_id`,`target_id`,`target_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : target_communications
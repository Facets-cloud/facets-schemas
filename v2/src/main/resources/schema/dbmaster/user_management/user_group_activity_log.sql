
-- start  Schema : user_group_activity_log

CREATE TABLE `user_group_activity_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `group_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `entity_type` enum('REGULAR','RETURN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `entity_id` bigint(20) NOT NULL,
  `activity_time` timestamp NULL DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_valid` int(11) DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_group` (`org_id`,`group_id`),
  KEY `idx_user` (`org_id`,`user_id`,`activity_time`),
  KEY `idx_entity` (`entity_type`,`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : user_group_activity_log

-- start  Schema : requests

CREATE TABLE `requests` (
  `id` int(11) NOT NULL,
  `status` enum('PENDING','APPROVED','REJECTED','CUSTOM') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `created_by` int(11) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_by` int(11) DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type` enum('CHANGE_IDENTIFIER','GOODWILL','TRANSACTION_UPDATE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `params` mediumtext COLLATE utf8mb4_unicode_ci,
  `is_one_step_change` tinyint(1) DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_user_idx` (`org_id`,`user_id`),
  KEY `org_status_idx` (`org_id`,`status`),
  KEY `org_created_idx` (`org_id`,`created_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : requests
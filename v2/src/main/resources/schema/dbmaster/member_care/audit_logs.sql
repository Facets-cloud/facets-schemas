
-- start  Schema : audit_logs

CREATE TABLE `audit_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_id` int(11) DEFAULT NULL,
  `status` enum('PENDING','SUCCESS','FAILED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) DEFAULT NULL,
  `assoc_id` int(11) DEFAULT NULL,
  `api_http_method` enum('GET','POST') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `api_resource` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `api_method` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `api_status` int(11) DEFAULT NULL,
  `api_item_status` int(11) DEFAULT NULL,
  `api_request_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apache_thread_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `source_ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `useragent` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_user_idx` (`org_id`,`user_id`),
  KEY `org_creator_idx` (`org_id`,`created_by`),
  KEY `org_assoc_id_idx` (`org_id`,`assoc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : audit_logs
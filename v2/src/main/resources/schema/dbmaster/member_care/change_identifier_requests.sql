
-- start  Schema : change_identifier_requests

CREATE TABLE `change_identifier_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request_id` int(11) DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `old_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `new_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `entity` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sec_user_id` int(11) DEFAULT NULL,
  `auto_approve_type` enum('QUERY_PARAM','CONFIG','DISABLED','QUERY_DISABLED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` enum('EMAIL','MOBILE','EXTERNAL_ID','MERGE','ADDRESS','MOBILE_REALLOC') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('PROCESSING','SUCCESS','FAILED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_comments` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_type_idx` (`org_id`,`type`),
  KEY `org_req_idx` (`org_id`,`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : change_identifier_requests
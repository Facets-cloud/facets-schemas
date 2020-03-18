
-- start  Schema : change_request_history

CREATE TABLE `change_request_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `current_value` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `requested_value` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_source` enum('INTOUCH','CLIENT','SOCIAL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CLIENT',
  `request_type` enum('MOBILE','EXTERNAL_ID','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MOBILE',
  `request_status` enum('APPROVED','PENDING') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `requested_by` bigint(20) NOT NULL,
  `request_time` datetime NOT NULL,
  `approved_by` bigint(20) NOT NULL,
  `last_changed_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`user_id`,`current_value`,`requested_value`,`request_type`),
  KEY `org_id_3` (`org_id`,`user_id`),
  KEY `org_id_4` (`org_id`,`request_type`,`request_status`),
  KEY `request_status` (`request_status`),
  KEY `org_id_5` (`org_id`,`request_source`,`request_type`,`request_status`),
  KEY `org_id_6` (`org_id`,`request_source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='a log of the requests made';


-- end  Schema : change_request_history
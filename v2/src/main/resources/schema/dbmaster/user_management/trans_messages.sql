
-- start  Schema : trans_messages

CREATE TABLE `trans_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `message_id` bigint(20) NOT NULL COMMENT 'Refers message id from nsadmin messages table',
  `user_id` bigint(11) NOT NULL DEFAULT '-1',
  `receiver` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `type` enum('EMAIL','SMS') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SMS',
  `priority` enum('HIGH','DEFAULT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `context_type` enum('REGISTRATION','TRANSACTION','POINTS','VOUCHER','VALIDATION','GENERAL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'GENERAL',
  `context_id` int(11) NOT NULL DEFAULT '-1',
  `sent_by` int(11) NOT NULL COMMENT 'Store id from which the message was sent',
  `sent_on` datetime NOT NULL,
  `updated_on` datetime DEFAULT NULL,
  `client_id` int(11) NOT NULL COMMENT 'Refers to the nsadmin client',
  `status` enum('SUBMITTED','SENT','DELIVERED','FAILED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SUBMITTED',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Mainly for failure',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `msg_uidx` (`message_id`),
  KEY `org_user_idx` (`org_id`,`user_id`),
  KEY `org_mobile_idx` (`org_id`,`receiver`),
  KEY `org_context_status_idx` (`org_id`,`context_type`,`status`),
  KEY `org_status_idx` (`org_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : trans_messages
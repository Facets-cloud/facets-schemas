
-- start  Schema : issue_tracker

CREATE TABLE `issue_tracker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `priority` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `department` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assigned_to` bigint(20) NOT NULL,
  `issue_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `issue_name` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` varchar(13) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ticket_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assigned_by` bigint(20) NOT NULL,
  `due_date` date NOT NULL,
  `created_date` datetime NOT NULL,
  `mark_critical_on` datetime DEFAULT NULL,
  `reported_by` enum('EMAIL','INTOUCH','CALLCENTER','CLIENT','MICROSITE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'INTOUCH',
  `type` enum('STORE','CUSTOMER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CUSTOMER',
  `resolved_by` bigint(20) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_by` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`ticket_code`),
  KEY `org_id_2` (`org_id`,`customer_id`),
  KEY `org_id_3` (`org_id`,`assigned_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : issue_tracker

-- start  Schema : issue_tracker_log

CREATE TABLE `issue_tracker_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tracker_id` bigint(20) NOT NULL,
  `org_id` int(11) NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `priority` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `department` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assigned_to` bigint(20) NOT NULL,
  `issue_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `issue_name` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `ticket_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assigned_by` bigint(20) NOT NULL,
  `due_date` datetime NOT NULL,
  `reported_by` enum('INTOUCH','CALLCENTER','CLIENT','MICROSITE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'INTOUCH',
  `resolved_by` bigint(20) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `tracker_id` (`tracker_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : issue_tracker_log
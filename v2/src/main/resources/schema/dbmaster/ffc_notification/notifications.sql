
-- start  Schema : notifications

CREATE TABLE `notifications` (
  `id` int(100) NOT NULL,
  `notification_config_id` int(100) NOT NULL,
  `type` enum('DATA-SANITY','UNDER-PERFORMANCE','MILESTONE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `kpis` enum('VISITOR','BILLS','CONVERSION','SALES') COLLATE utf8mb4_unicode_ci NOT NULL,
  `priority_type` enum('INFO','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `target` int(30) NOT NULL,
  `org_id` int(20) NOT NULL,
  `user_id` int(100) NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_enitity_type` enum('ZONE','CONCEPT','STORE','STR_SERVER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `sent_time` timestamp NULL DEFAULT NULL,
  `status` enum('SENT','FAILED','SUCCESSFUL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `readAt` timestamp NULL DEFAULT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `header` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `defaulter_entites` varchar(700) COLLATE utf8mb4_unicode_ci NOT NULL,
  `channel` enum('PUSH','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PUSH',
  `createdBy` int(50) DEFAULT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : notifications
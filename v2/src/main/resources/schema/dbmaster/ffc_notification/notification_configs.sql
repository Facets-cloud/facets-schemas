
-- start  Schema : notification_configs

CREATE TABLE `notification_configs` (
  `id` int(100) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `orgId` int(50) NOT NULL,
  `type` enum('DATA-SANITY','UNDER-PERFORMANCE','MILESTONE-ACHIEVED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `kpi` enum('VISITOR','BILLS','CONVERSION','SALES') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `priority_type` enum('INFO','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'INFO',
  `target` int(30) DEFAULT NULL,
  `selfRoles` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parentRoles` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `scheduledInterval` int(50) DEFAULT NULL,
  `scheduledTime` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : notification_configs
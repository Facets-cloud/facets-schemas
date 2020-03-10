
-- start  Schema : installation_details

CREATE TABLE `installation_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` bigint(20) NOT NULL,
  `org_id` bigint(11) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `engagement_type` enum('rollout','pilot','nightly_testing') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_dispatched` datetime DEFAULT NULL,
  `date_installed` datetime DEFAULT NULL,
  `date_callback` datetime DEFAULT NULL,
  `device_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dispatch_type` enum('hand_delivered','courier') COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  `store_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2061 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : installation_details

-- start  Schema : device_registration_meta

CREATE TABLE `device_registration_meta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` bigint(20) DEFAULT NULL,
  `device_type` enum('VM','CE','FFC','HeatMap') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `wifi_mac_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lan_mac_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `till_id` bigint(20) DEFAULT '0',
  `store_id` bigint(20) DEFAULT '0',
  `org_id` bigint(20) DEFAULT '0',
  `device_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `processor` enum('RaspberryPi_3B','RaspberryPi_3B+') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lens` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `with_staff_switch` tinyint(1) DEFAULT '0',
  `case_version` enum('Acrylic Big','Acrylic Small','ABS Big','ABS Small') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `case_color` enum('black','white','blue') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` smallint(6) DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `notes` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : device_registration_meta
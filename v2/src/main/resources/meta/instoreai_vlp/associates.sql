
-- start  Schema : associates

CREATE TABLE `associates` (
  `id` int(20) NOT NULL,
  `org_id` int(15) NOT NULL,
  `first_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mobile` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `store_id` int(15) NOT NULL,
  `updated_on` datetime NOT NULL,
  `added_on` datetime NOT NULL,
  `added_by` int(15) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_registered` tinyint(1) NOT NULL DEFAULT '0',
  `registration_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Not Started',
  `imageQuality` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `is_registered` (`is_registered`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : associates
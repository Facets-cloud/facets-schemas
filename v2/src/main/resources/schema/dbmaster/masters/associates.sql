
-- start  Schema : associates

CREATE TABLE `associates` (
  `id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `associate_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `firstname` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastname` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `store_id` int(11) NOT NULL,
  `store_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_by` int(11) NOT NULL,
  `updated_on` datetime NOT NULL,
  `added_on` datetime NOT NULL,
  `added_by` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `cashier_code_uidx` (`org_id`,`associate_code`,`store_code`),
  UNIQUE KEY `cashier_code2_uidx` (`org_id`,`associate_code`,`store_id`),
  UNIQUE KEY `store_cashier_idx` (`org_id`,`store_id`,`associate_code`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : associates
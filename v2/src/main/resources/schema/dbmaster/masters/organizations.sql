
-- start  Schema : organizations

CREATE TABLE `organizations` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `fiscal_year_start` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` int(1) NOT NULL DEFAULT '0',
  `base_language_id` int(11) NOT NULL DEFAULT '0',
  `base_currency_id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `default_time_zone_id` int(11) NOT NULL,
  `min_sms_hour` int(11) DEFAULT '9',
  `max_sms_hour` int(11) NOT NULL DEFAULT '21',
  `is_ndnc` tinyint(1) NOT NULL DEFAULT '0',
  `optin_active` tinyint(1) NOT NULL DEFAULT '0',
  `time_zone` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '+05:30',
  `reporting_email` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `language_id` (`base_language_id`),
  KEY `currency_id` (`base_currency_id`),
  KEY `default_time_zone_id` (`default_time_zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : organizations
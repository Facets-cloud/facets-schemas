
-- start  Schema : campaigns_base

CREATE TABLE `campaigns_base` (
  `id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ga_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ga_source_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `roi_report_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `campaign_roi_type_id` int(11) NOT NULL DEFAULT '1',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` bigint(20) NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `issue_vouchers` tinyint(1) NOT NULL DEFAULT '0',
  `voucher_series_id` varchar(600) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `points_properties_id` int(11) DEFAULT '-1',
  `created_by` bigint(20) NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '0',
  `is_ga_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `is_test_control_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `created` datetime NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `test_control` enum('ORG','CUSTOM','SKIP') COLLATE utf8mb4_unicode_ci DEFAULT 'ORG',
  `test_percentage` int(11) DEFAULT NULL,
  `additional_properties` mediumtext COLLATE utf8mb4_unicode_ci,
  `is_migrated` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`voucher_series_id`),
  KEY `auto_time_idx` (`modified`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : campaigns_base
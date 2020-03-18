
-- start  Schema : coupon_upload

CREATE TABLE `coupon_upload` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `status` enum('STARTED','VALIDATED','COMMITED','ERRORED','FINISHED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STARTED',
  `total_upload_count` int(11) NOT NULL DEFAULT '0',
  `error_count` int(11) NOT NULL DEFAULT '0',
  `error_code` int(11) DEFAULT NULL,
  `error_message` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uploaded_file_url` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_file_url` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `success_file_url` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `temp_table_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `created_on` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_on` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uploaded_file_name` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id_coupon_series_id` (`org_id`,`coupon_series_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_upload
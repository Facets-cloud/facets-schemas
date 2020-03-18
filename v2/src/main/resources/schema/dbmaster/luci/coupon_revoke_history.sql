
-- start  Schema : coupon_revoke_history

CREATE TABLE `coupon_revoke_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `status` enum('STARTED','ERRORRED','FINISHED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STARTED',
  `coupon_revoke_type` enum('COUPON_SERIES','ONLY_UNISSUED','CUSTOMER_ID','COUPON_CODE','COUPON_ID','CUSTOMER_AND_COUPON') COLLATE utf8mb4_unicode_ci NOT NULL,
  `input_table_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `temp_table_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `input_count` int(11) NOT NULL DEFAULT '0',
  `invalid_count` int(11) NOT NULL DEFAULT '0',
  `unrevoked_count` int(11) NOT NULL DEFAULT '0',
  `revoked_count` int(11) NOT NULL DEFAULT '0',
  `uploaded_file_name` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uploaded_file_handle` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_file_handle` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `success_file_handle` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_on` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id_coupon_series_id` (`org_id`,`coupon_series_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_revoke_history
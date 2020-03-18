
-- start  Schema : coupon_download

CREATE TABLE `coupon_download` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `status` enum('STARTED','ERRORED','FINISHED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STARTED',
  `s3_file_path` tinytext COLLATE utf8mb4_unicode_ci,
  `error_code` int(11) DEFAULT NULL,
  `error_message` text COLLATE utf8mb4_unicode_ci,
  `requested_by` int(11) NOT NULL,
  `total_download_count` int(11) NOT NULL DEFAULT '0',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `type` enum('ISSUED','REDEEMED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_on` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id_coupon_series_id` (`org_id`,`coupon_series_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_download
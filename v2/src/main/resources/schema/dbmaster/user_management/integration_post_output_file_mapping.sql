
-- start  Schema : integration_post_output_file_mapping

CREATE TABLE `integration_post_output_file_mapping` (
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `file_id` int(11) NOT NULL,
  `created_by` int(11) NOT NULL,
  `file_type` enum('integration_post_output_points_redemption','integration_post_output_voucher_redemption','integration_post_output_voucher_issue','integration_post_output_customer_register','integration_post_output_customer_update','integration_post_output_bill_submit','integration_post_output_auto_configure','integration_post_output_nightly_sync','integration_post_output_eod_sync','integration_post_output_pre_auto_configure','integration_post_output_pre_nightly_sync','integration_post_output_pre_eod_sync') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  KEY `org_id` (`org_id`,`store_id`,`file_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : integration_post_output_file_mapping
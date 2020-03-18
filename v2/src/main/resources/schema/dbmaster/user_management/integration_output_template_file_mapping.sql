
-- start  Schema : integration_output_template_file_mapping

CREATE TABLE `integration_output_template_file_mapping` (
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `file_id` int(11) NOT NULL,
  `created_by` int(11) NOT NULL,
  `file_type` enum('integration_output_points_redemption','integration_output_voucher_redemption','integration_output_voucher_issue') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`org_id`,`store_id`,`file_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : integration_output_template_file_mapping
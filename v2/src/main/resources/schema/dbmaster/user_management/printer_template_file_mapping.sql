
-- start  Schema : printer_template_file_mapping

CREATE TABLE `printer_template_file_mapping` (
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `file_id` int(11) NOT NULL,
  `created_by` int(11) NOT NULL,
  `file_type` enum('bill','dvs_voucher','campaign_voucher','customer') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`org_id`,`store_id`,`file_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : printer_template_file_mapping
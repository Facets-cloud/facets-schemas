
-- start  Schema : coupon_upload_history

CREATE TABLE `coupon_upload_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `params` mediumtext COLLATE utf8mb4_unicode_ci,
  `campaign_id` int(10) DEFAULT NULL,
  `vsid` int(10) DEFAULT NULL,
  `import_type` enum('mobile','userid') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `temp_table_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` int(20) NOT NULL,
  `uploaded_by` int(20) NOT NULL,
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : coupon_upload_history
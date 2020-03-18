
-- start  Schema : bulk_upload_rule_error

CREATE TABLE `bulk_upload_rule_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `file_id` int(100) NOT NULL COMMENT 'reference of the bulk upload file',
  `expression` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'rule expression',
  `expression_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'rule expression json',
  `voucher_series_properties_success_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'voucher series properties for success case',
  `voucher_series_properties_failure_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'voucher series properties for failure case',
  `failure_reason` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'reason for failure',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bulk_upload_rule_error
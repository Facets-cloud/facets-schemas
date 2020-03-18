
-- start  Schema : mlm_bills_processed

CREATE TABLE `mlm_bills_processed` (
  `org_id` bigint(20) NOT NULL,
  `loyalty_log_id` bigint(20) NOT NULL,
  `bill_number` varchar(55) COLLATE utf8mb4_unicode_ci NOT NULL,
  `processed_time` datetime NOT NULL,
  `awarded_ids` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'JSON Array',
  `awarded_names` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Comma Separated Names',
  `total_points_awarded` float NOT NULL,
  PRIMARY KEY (`org_id`,`loyalty_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mlm_bills_processed
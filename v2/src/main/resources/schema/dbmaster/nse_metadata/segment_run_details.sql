
-- start  Schema : segment_run_details

CREATE TABLE `segment_run_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `segment_id` bigint(20) NOT NULL,
  `metadata_id` bigint(20) NOT NULL,
  `status` enum('VALIDATING','AWAITING_ACTION','VALIDATION_FAILED','DISCARDED','SYNC_INPROGRESS','SYNC_DONE','SYNC_FAILED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `success_source` text COLLATE utf8mb4_unicode_ci,
  `error_source` text COLLATE utf8mb4_unicode_ci,
  `stats_params` text COLLATE utf8mb4_unicode_ci,
  `last_updated_by` bigint(20) DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `segment_index` (`id`,`segment_id`) USING BTREE,
  KEY `org_segment_index` (`org_id`,`segment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_run_details
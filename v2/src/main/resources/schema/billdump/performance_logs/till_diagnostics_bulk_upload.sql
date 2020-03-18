
-- start  Schema : till_diagnostics_bulk_upload

CREATE TABLE `till_diagnostics_bulk_upload` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `till_diagnostics_fkey` int(11) NOT NULL COMMENT 'PKEY from from till_diagnostics table',
  `upload_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Bulk upload type customer, bill, nibill’',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'bulk upload status',
  `sync_time` datetime DEFAULT NULL COMMENT 'time when sync completed',
  `inserted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `till_diagnostics_fkey` (`till_diagnostics_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=1696756 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : till_diagnostics_bulk_upload
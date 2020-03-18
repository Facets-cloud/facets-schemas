
-- start  Schema : store_server_bulk_upload

CREATE TABLE `store_server_bulk_upload` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `upload_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Bulk upload type customer, bill, nibill',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'bulk upload status',
  `sync_time` datetime DEFAULT NULL COMMENT 'time when sync completed',
  `inserted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ss_health_fkey` int(11) DEFAULT '-1' COMMENT 'fkey from store_server_health table',
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `store_idx` (`ss_health_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=16764433 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_server_bulk_upload
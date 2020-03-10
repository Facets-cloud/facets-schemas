
-- start  Schema : import_point_engine_status

CREATE TABLE `import_point_engine_status` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `import_id` int(11) unsigned NOT NULL,
  `point_engine_batch_id` int(10) unsigned NOT NULL,
  `status` enum('SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `temp_table_start_id` int(10) unsigned DEFAULT '0',
  `temp_table_end_id` int(10) unsigned DEFAULT NULL,
  `point_engine_status_details` mediumtext COLLATE utf8mb4_unicode_ci,
  `last_update_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `import_id_batch_id` (`import_id`,`point_engine_batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_point_engine_status
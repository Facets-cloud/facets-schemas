
-- start  Schema : merge_process_log

CREATE TABLE `merge_process_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `handler_id` tinyint(3) NOT NULL,
  `handler_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('START','PROCESSING','ROLLEDBACK','SUCCESS','FAILED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_details` longtext COLLATE utf8mb4_unicode_ci,
  `filehandle` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `executed_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_taken_in_millis` int(11) NOT NULL DEFAULT '0',
  `is_file_valid` tinyint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `request_id` (`org_id`,`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : merge_process_log
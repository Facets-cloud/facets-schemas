
-- start  Schema : api_summary_table

CREATE TABLE `api_summary_table` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `resource` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `method` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `api_version` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `total_hit_count` int(10) unsigned DEFAULT NULL,
  `total_response_time` double NOT NULL,
  `total_success_count` int(11) NOT NULL,
  `total_failure_count` int(11) NOT NULL,
  `summary_date` datetime NOT NULL,
  `gen_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : api_summary_table
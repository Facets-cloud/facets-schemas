
-- start  Schema : store_server_till_reports

CREATE TABLE `store_server_till_reports` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `till_id` int(11) DEFAULT NULL,
  `last_request` datetime DEFAULT NULL COMMENT 'Timestamp when last request was sent',
  `requests_sent` int(11) DEFAULT NULL COMMENT 'no of requests raised',
  `responses_received` int(11) DEFAULT NULL COMMENT 'no of responses received',
  `avg_time_taken_per_call` double DEFAULT NULL,
  `last_updated_at` datetime NOT NULL COMMENT 'record insert time',
  `ss_health_fkey` int(11) DEFAULT '-1' COMMENT 'fkey from store_server_health table',
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`till_id`),
  KEY `last_updated_at` (`last_updated_at`),
  KEY `ssfkey` (`ss_health_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=5336478 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_server_till_reports
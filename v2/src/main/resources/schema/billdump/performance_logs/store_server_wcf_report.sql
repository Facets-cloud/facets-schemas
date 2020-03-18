
-- start  Schema : store_server_wcf_report

CREATE TABLE `store_server_wcf_report` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `requests_sent` int(11) DEFAULT NULL COMMENT 'no of requests raised',
  `last_request` datetime DEFAULT NULL COMMENT 'Timestamp when last request was sent',
  `requests_received` int(11) DEFAULT NULL COMMENT 'no of requests received',
  `inserted_at` datetime NOT NULL COMMENT 'record insert time',
  `version` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'WCF version',
  `ss_health_fkey` int(11) DEFAULT '-1' COMMENT 'fkey from store_server_health table',
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `inserted_at` (`inserted_at`),
  KEY `ssfkey` (`ss_health_fkey`)
) ENGINE=InnoDB AUTO_INCREMENT=842911 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_server_wcf_report
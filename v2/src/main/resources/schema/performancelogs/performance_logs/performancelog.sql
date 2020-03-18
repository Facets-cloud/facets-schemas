
-- start  Schema : performancelog

CREATE TABLE `performancelog` (
  `id` int(18) NOT NULL AUTO_INCREMENT,
  `time_stamp` datetime NOT NULL,
  `apache_thread_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('API','WEB','SMS_IN','CLI','THRIFT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'WEB',
  `org_id` int(8) NOT NULL,
  `store_id` int(11) NOT NULL,
  `module` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `action` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `time_taken` float NOT NULL,
  `init_mem` float NOT NULL,
  `end_mem` float NOT NULL,
  `peak_mem` float NOT NULL,
  `init_cpu` int(11) NOT NULL,
  `final_cpu` int(11) NOT NULL,
  `cache_hit` int(1) DEFAULT NULL,
  `action_dump` mediumtext COLLATE utf8mb4_unicode_ci,
  `status` enum('OPEN','CLOSED','REDIRECTED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `count` int(11) DEFAULT NULL,
  `time_breakup` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`type`),
  KEY `time_stamp` (`time_stamp`),
  KEY `Thread_id` (`apache_thread_id`),
  KEY `action` (`action`,`type`),
  KEY `org_id_2` (`org_id`,`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : performancelog
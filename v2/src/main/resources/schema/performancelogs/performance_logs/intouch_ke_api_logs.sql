
-- start  Schema : intouch_ke_api_logs

CREATE TABLE `intouch_ke_api_logs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `client_ip` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `server_ip` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_agent_id` int(11) NOT NULL,
  `resource` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `method` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hit_time` datetime NOT NULL,
  `response_time` bigint(20) NOT NULL,
  `http_code` smallint(3) DEFAULT NULL,
  `success_count` int(11) DEFAULT NULL,
  `failure_count` int(11) DEFAULT NULL,
  `status_codes` text CHARACTER SET utf8 NOT NULL,
  `primary_updated_info` text CHARACTER SET utf8,
  `other_updated_info` text CHARACTER SET utf8,
  `error_messages` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `request_id` (`request_id`),
  KEY `org_id` (`org_id`,`user_id`),
  KEY `hit_time` (`hit_time`)
) ENGINE=InnoDB AUTO_INCREMENT=361407666 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED;


-- end  Schema : intouch_ke_api_logs
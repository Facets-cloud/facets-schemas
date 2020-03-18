
-- start  Schema : api_hit_table

CREATE TABLE `api_hit_table` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `request_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apache_req_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `method` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `hit_time` datetime NOT NULL,
  `client_ip` bigint(20) NOT NULL,
  `response_time` float NOT NULL,
  `success_count` int(11) DEFAULT NULL,
  `failure_count` int(11) DEFAULT NULL,
  `api_version` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `query_params` longtext COLLATE utf8mb4_unicode_ci,
  `http_method` enum('GET','POST') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_agent_id` int(11) NOT NULL,
  `status_codes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `server_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `request_id` (`request_id`),
  KEY `org_id` (`org_id`,`user_id`),
  KEY `hit_time` (`hit_time`),
  KEY `apache_req_id` (`apache_req_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25493570 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : api_hit_table
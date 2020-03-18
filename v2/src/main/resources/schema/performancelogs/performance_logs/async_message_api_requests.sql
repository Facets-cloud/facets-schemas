
-- start  Schema : async_message_api_requests

CREATE TABLE `async_message_api_requests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `request_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_time` datetime NOT NULL,
  `status` enum('QUEUED','RUNNING','CANCELLED','FINISHED','FAILED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_body` mediumtext COLLATE utf8mb4_unicode_ci,
  `request_headers` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `response_status` mediumtext COLLATE utf8mb4_unicode_ci,
  `response_body` mediumtext COLLATE utf8mb4_unicode_ci,
  `response_headers` mediumtext COLLATE utf8mb4_unicode_ci,
  `request_path` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_method` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` datetime NOT NULL,
  PRIMARY KEY (`id`,`request_id`),
  KEY `org_id` (`org_id`,`request_id`),
  KEY `request_time` (`request_time`),
  KEY `request_id` (`request_id`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : async_message_api_requests
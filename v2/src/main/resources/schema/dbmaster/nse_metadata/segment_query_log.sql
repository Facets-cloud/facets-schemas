
-- start  Schema : segment_query_log

CREATE TABLE `segment_query_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `request` text,
  `request_type_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `request_start_time` datetime NOT NULL,
  `request_end_time` datetime NOT NULL,
  `query_status` enum('SUCCESS','FAILURE') NOT NULL,
  `failure_code` int(11) NOT NULL DEFAULT '200',
  PRIMARY KEY (`id`),
  KEY `ord_id` (`id`,`org_id`),
  KEY `request_type` (`request_type_id`),
  KEY `client_request_type` (`client_id`,`request_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- end  Schema : segment_query_log
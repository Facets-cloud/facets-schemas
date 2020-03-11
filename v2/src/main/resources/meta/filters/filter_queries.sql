
-- start  Schema : filter_queries

CREATE TABLE `filter_queries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `session_id` int(11) NOT NULL,
  `filter_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `filter_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `query` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sql_explain` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `time_taken_secs` int(11) NOT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `session_id` (`session_id`),
  KEY `time_taken_secs` (`time_taken_secs`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='logs all filter queries';


-- end  Schema : filter_queries
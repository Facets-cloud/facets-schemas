
-- start  Schema : odip_ddl_queries

CREATE TABLE `odip_ddl_queries` (
  `pod` varchar(30) NOT NULL,
  `cluster` varchar(30) DEFAULT NULL,
  `table_name` varchar(100) NOT NULL,
  `query` text NOT NULL,
  `executed_query` text NOT NULL,
  `lego_executed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `started_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `completed_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` varchar(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- end  Schema : odip_ddl_queries
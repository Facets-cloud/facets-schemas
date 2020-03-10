
-- start  Schema : org_query_sync_details

CREATE TABLE `org_query_sync_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `cron_id` int(11) DEFAULT NULL,
  `request_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `org_tables_sync_id` int(11) NOT NULL,
  `table_name` varchar(255) NOT NULL,
  `query` text NOT NULL,
  `rows_updated` bigint(20) NOT NULL,
  `num_rows_selected` bigint(20) NOT NULL,
  `execution_time` bigint(20) NOT NULL,
  `select_time` bigint(20) NOT NULL,
  `ssh_copy_time` bigint(20) NOT NULL,
  `temp_table_load_time` bigint(20) NOT NULL,
  `temp_table_join_time` bigint(20) NOT NULL,
  `is_successfull` tinyint(1) NOT NULL,
  `error_message` text,
  `error_details` text,
  PRIMARY KEY (`id`),
  KEY `org_tables_sync_id_index` (`org_tables_sync_id`),
  KEY `org_id_index` (`org_id`),
  KEY `table_name_index` (`table_name`),
  KEY `is_successfull_index` (`is_successfull`),
  KEY `cron_id_index` (`cron_id`),
  KEY `request_id_index` (`request_id`),
  KEY `start_time_index` (`start_time`)
) ENGINE=MyISAM AUTO_INCREMENT=4840530 DEFAULT CHARSET=utf8;


-- end  Schema : org_query_sync_details
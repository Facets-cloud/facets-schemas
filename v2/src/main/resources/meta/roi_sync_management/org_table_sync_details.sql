
-- start  Schema : org_table_sync_details

CREATE TABLE `org_table_sync_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_sync_details_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `cron_id` int(11) DEFAULT NULL,
  `request_id` int(11) DEFAULT NULL,
  `table_name` varchar(255) NOT NULL COMMENT 'campaign name',
  `is_fact` tinyint(1) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `execution_time` bigint(20) DEFAULT NULL,
  `is_finished` tinyint(1) NOT NULL,
  `is_successfull` tinyint(1) NOT NULL,
  `num_new_rows` bigint(20) DEFAULT NULL COMMENT 'Number of New Bills',
  `was_incremental` tinyint(1) NOT NULL,
  `schema_refreshed` tinyint(1) NOT NULL,
  `multiple_last_inserts` tinyint(1) DEFAULT NULL,
  `last_insert_key` varchar(255) DEFAULT NULL,
  `begin_last_insert_id` bigint(20) DEFAULT NULL COMMENT 'Number of contacted users before sync',
  `begin_last_insert_map` text,
  `end_last_insert_id` bigint(20) DEFAULT NULL COMMENT 'Number of contacted users after sync',
  `end_last_insert_map` text,
  `error_message` text,
  `error_details` text,
  PRIMARY KEY (`id`),
  KEY `org_sync_details_id_index` (`org_sync_details_id`),
  KEY `org_id_index` (`org_id`),
  KEY `table_name_index` (`table_name`),
  KEY `start_time_index` (`start_time`),
  KEY `is_finished_index` (`is_finished`),
  KEY `is_successfull_index` (`is_successfull`),
  KEY `cron_id_index` (`cron_id`),
  KEY `request_id_index` (`request_id`)
) ENGINE=MyISAM AUTO_INCREMENT=209988 DEFAULT CHARSET=latin1;


-- end  Schema : org_table_sync_details
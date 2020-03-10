
-- start  Schema : org_table_fields_sync_details

CREATE TABLE `org_table_fields_sync_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `cron_id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `org_table_sync_details_id` bigint(20) NOT NULL,
  `table_name` varchar(255) NOT NULL,
  `field_name` text NOT NULL,
  `field_scope` varchar(255) NOT NULL,
  `is_new_field` tinyint(1) NOT NULL,
  `was_complete_refresh` tinyint(1) NOT NULL,
  `num_new_values` bigint(20) DEFAULT NULL,
  `num_total_values` bigint(20) DEFAULT NULL,
  `is_successfull` tinyint(1) DEFAULT '1',
  `error_message` text,
  `error_details` text,
  PRIMARY KEY (`id`),
  KEY `org_table_sync_details_id_index` (`org_table_sync_details_id`),
  KEY `org_id_index` (`org_id`),
  KEY `cron_id_index` (`cron_id`),
  KEY `request_id_index` (`request_id`),
  KEY `table_name_index` (`table_name`),
  KEY `field_scope_index` (`field_scope`),
  KEY `is_successfull_index` (`is_successfull`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


-- end  Schema : org_table_fields_sync_details
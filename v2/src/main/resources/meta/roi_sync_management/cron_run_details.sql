
-- start  Schema : cron_run_details

CREATE TABLE `cron_run_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cron_name` varchar(255) NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `slave_lag` bigint(20) NOT NULL,
  `start_data_until` datetime DEFAULT NULL,
  `is_finished` tinyint(1) NOT NULL,
  `finished_with_error` tinyint(1) NOT NULL,
  `total_run_time` bigint(20) NOT NULL,
  `multi_threaded` tinyint(1) NOT NULL,
  `num_threads_per_db` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cron_name_index` (`cron_name`),
  KEY `start_time_index` (`start_time`),
  KEY `is_finished_index` (`is_finished`),
  KEY `finished_with_error_index` (`finished_with_error`)
) ENGINE=MyISAM AUTO_INCREMENT=633 DEFAULT CHARSET=latin1;


-- end  Schema : cron_run_details
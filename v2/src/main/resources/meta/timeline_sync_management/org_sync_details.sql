
-- start  Schema : org_sync_details

CREATE TABLE `org_sync_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `cron_id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `is_finished` tinyint(1) NOT NULL,
  `execution_time` bigint(20) NOT NULL,
  `is_successfull` tinyint(1) NOT NULL,
  `is_interrupted` tinyint(1) NOT NULL,
  `was_incremental` tinyint(1) NOT NULL,
  `error_message` text,
  `error_details` text,
  `latest_bill_date` datetime DEFAULT NULL,
  `latest_registration_date` datetime DEFAULT NULL,
  `new_bills_imported` int(11) NOT NULL,
  `new_customers_imported` int(11) NOT NULL,
  `new_lineitems_imported` int(11) NOT NULL,
  `new_slab_upgrades_imported` int(11) NOT NULL,
  `new_vouchers_imported` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id_index` (`org_id`),
  KEY `cron_id_index` (`cron_id`),
  KEY `request_id_index` (`request_id`),
  KEY `start_time_index` (`start_time`),
  KEY `is_finished_index` (`is_finished`),
  KEY `is_successfull_index` (`is_successfull`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : org_sync_details
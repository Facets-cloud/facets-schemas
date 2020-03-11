
-- start  Schema : m_scheduled_runs

CREATE TABLE `m_scheduled_runs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` enum('REPORT','DASHBOARD','CAMPAIGN','BNS','TEST') NOT NULL DEFAULT 'REPORT',
  `report_id` int(10) DEFAULT NULL,
  `org_id` int(10) NOT NULL,
  `creator_intouch_id` int(11) NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  `schedule_frequency` enum('IMMEDIATE','DAILY','WEEKLY','MONTHLY') NOT NULL,
  `offset` int(10) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `no_of_times_to_execute` mediumint(4) NOT NULL,
  `schedule_processed_until` datetime NOT NULL,
  `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creation_date` date NOT NULL,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `creator_intouch_id` (`creator_intouch_id`,`org_id`,`report_id`),
  KEY `org_id` (`org_id`,`is_active`,`is_valid`,`is_deleted`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;


-- end  Schema : m_scheduled_runs
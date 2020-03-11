
-- start  Schema : m_scheduled_run_log

CREATE TABLE `m_scheduled_run_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` enum('REPORT','DASHBOARD','CAMPAIGN','BNS','TEST') NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `org_id` int(10) NOT NULL,
  `report_id` int(10) DEFAULT NULL,
  `schedule_id` int(10) NOT NULL,
  `report_name` varchar(256) NOT NULL,
  `report_category` varchar(256) NOT NULL,
  `access_based_broken_parts` int(11) NOT NULL,
  `run_at_access_level` enum('STORE_LEVEL','ZONE_LEVEL','ORG_ADMIN') DEFAULT NULL,
  `accessible_entities` mediumtext,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `calculated_schedule_date` datetime NOT NULL,
  `processed_until_date_at_execution` datetime NOT NULL,
  `nsAdminTransactionId` mediumtext,
  `performed_action` enum('EMAIL','SMS','FTP') DEFAULT NULL,
  `action_metadata` longtext NOT NULL,
  `shared_with` mediumtext,
  `is_success` tinyint(1) NOT NULL,
  `file_handle` longtext,
  `stack_trace` mediumtext,
  `error_message` varchar(512) DEFAULT NULL,
  `failure_type` enum('REPORT_EXECUTION','REPORT_OUTPUT_EXTRACTION','FILE_UPLOAD','SENDING_EMAIL','CAMPAIGN_ROI_NOT_COMPUTED','EMAIL_NOT_FOUND','FTP_UPLOAD') DEFAULT NULL,
  `no_of_times_snapshot` int(10) NOT NULL,
  `schedule_start_snapshot` date NOT NULL,
  `schedule_end_snapshot` date NOT NULL,
  `offset_snapshot` mediumint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `start_time` (`start_time`),
  KEY `report_id` (`report_id`),
  KEY `org_id` (`org_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


-- end  Schema : m_scheduled_run_log

-- start  Schema : m_report_audit_log

CREATE TABLE `m_report_audit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `entity_type` enum('REPORT','SCHEDULE','CAMPAIGN_SCHEDULE','CAMPAIGN_ORG_METADATA') NOT NULL,
  `entity_id` int(11) NOT NULL,
  `context` varchar(256) NOT NULL,
  `action_taken` enum('CREATE','EDIT','DELETE','ACTIVATE','INACTIVATE') NOT NULL,
  `log_message` mediumtext,
  `logging_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`,`entity_type`,`entity_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_report_audit_log
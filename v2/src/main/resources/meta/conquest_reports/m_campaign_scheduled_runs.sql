
-- start  Schema : m_campaign_scheduled_runs

CREATE TABLE `m_campaign_scheduled_runs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `campaign_end_date` date NOT NULL,
  `campaign_type` enum('BULK') NOT NULL,
  `report_type` enum('ROI','DELIVERY') NOT NULL,
  `schedule_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_campaign` (`org_id`,`campaign_id`,`report_type`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


-- end  Schema : m_campaign_scheduled_runs
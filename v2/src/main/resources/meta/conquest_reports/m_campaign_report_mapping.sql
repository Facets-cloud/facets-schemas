
-- start  Schema : m_campaign_report_mapping

CREATE TABLE `m_campaign_report_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campaign_type` enum('BULK') NOT NULL,
  `report_type` enum('ROI','DELIVERY') NOT NULL,
  `report_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `report_type` (`campaign_type`,`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- end  Schema : m_campaign_report_mapping
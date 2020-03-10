
-- start  Schema : m_org_report_mapping

CREATE TABLE `m_org_report_mapping` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(10) unsigned NOT NULL,
  `report_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id_index` (`org_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2720 DEFAULT CHARSET=latin1;


-- end  Schema : m_org_report_mapping
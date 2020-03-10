
-- start  Schema : m_report_outputs

CREATE TABLE `m_report_outputs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `report_id` int(10) unsigned NOT NULL,
  `o_name` varchar(1024) NOT NULL,
  `o_display_name` varchar(1024) DEFAULT NULL,
  `o_desc` mediumtext,
  PRIMARY KEY (`id`),
  KEY `report_id_index` (`report_id`)
) ENGINE=MyISAM AUTO_INCREMENT=279330 DEFAULT CHARSET=utf8;


-- end  Schema : m_report_outputs
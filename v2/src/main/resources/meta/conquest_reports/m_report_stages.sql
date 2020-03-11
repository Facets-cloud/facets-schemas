
-- start  Schema : m_report_stages

CREATE TABLE `m_report_stages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `report_id` int(10) unsigned NOT NULL,
  `wf_id` int(10) unsigned NOT NULL,
  `s_seq_num` smallint(6) NOT NULL,
  `s_display_name` varchar(256) NOT NULL,
  `s_desc` mediumtext,
  PRIMARY KEY (`id`),
  KEY `report_id_index` (`report_id`),
  KEY `wf_id_index` (`wf_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_report_stages
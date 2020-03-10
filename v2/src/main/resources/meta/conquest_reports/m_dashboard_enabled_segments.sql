
-- start  Schema : m_dashboard_enabled_segments

CREATE TABLE `m_dashboard_enabled_segments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `segment_id` int(11) NOT NULL,
  `segment_name` varchar(256) NOT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- end  Schema : m_dashboard_enabled_segments
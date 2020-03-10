
-- start  Schema : backup_m_dashboard_view_settings

CREATE TABLE `backup_m_dashboard_view_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dashboard_view_id` int(11) NOT NULL,
  `key` varchar(255) NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dashboard_view_id` (`dashboard_view_id`)
) ENGINE=MyISAM AUTO_INCREMENT=229 DEFAULT CHARSET=latin1 COMMENT='Properties for the view display';


-- end  Schema : backup_m_dashboard_view_settings
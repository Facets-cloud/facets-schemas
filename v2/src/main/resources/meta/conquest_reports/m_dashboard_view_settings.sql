
-- start  Schema : m_dashboard_view_settings

CREATE TABLE `m_dashboard_view_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dashboard_view_id` int(11) NOT NULL,
  `key` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dashboard_view_id` (`dashboard_view_id`)
) ENGINE=InnoDB AUTO_INCREMENT=679 DEFAULT CHARSET=utf8 COMMENT='Properties for the view display';


-- end  Schema : m_dashboard_view_settings
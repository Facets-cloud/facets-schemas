
-- start  Schema : backup_m_dashboard_views

CREATE TABLE `backup_m_dashboard_views` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `report_id` int(11) NOT NULL,
  `output_key` varchar(255) NOT NULL,
  `time_unit` enum('day','week','month','year') NOT NULL,
  `position` int(11) NOT NULL COMMENT 'Position of the view',
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`,`category_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COMMENT='List of views present as part of view';


-- end  Schema : backup_m_dashboard_views

-- start  Schema : m_dashboard_views

CREATE TABLE `m_dashboard_views` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` mediumtext NOT NULL,
  `report_id` int(11) NOT NULL,
  `output_key` varchar(255) NOT NULL,
  `filter` enum('day','week','month','year','singleCamp','multiCamp','singleComm','multiComm','singleGroup','multiGroup','singleCampSMS','multiCampSMS','singleCommSMS','multiCommSMS','singleGroupSMS','multiGroupSMS') DEFAULT NULL,
  `position` int(11) NOT NULL COMMENT 'Position of the view',
  `is_default_enabled` tinyint(1) NOT NULL DEFAULT '1',
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`,`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='List of views present as part of view';


-- end  Schema : m_dashboard_views
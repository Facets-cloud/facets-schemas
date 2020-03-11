
-- start  Schema : m_dashboard_disabled_orgs

CREATE TABLE `m_dashboard_disabled_orgs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_dashboard_disabled_orgs
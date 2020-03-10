
-- start  Schema : m_dashboard_org_view_configs

CREATE TABLE `m_dashboard_org_view_configs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `view_id` int(11) NOT NULL,
  `is_enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_OrgId` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;


-- end  Schema : m_dashboard_org_view_configs
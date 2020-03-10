
-- start  Schema : m_dashboard_enabled_bandings

CREATE TABLE `m_dashboard_enabled_bandings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `banding_type` varchar(20) NOT NULL,
  `low_value` int(11) NOT NULL,
  `high_value` int(11) NOT NULL,
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- end  Schema : m_dashboard_enabled_bandings
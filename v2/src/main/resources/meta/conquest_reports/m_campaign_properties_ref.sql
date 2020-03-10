
-- start  Schema : m_campaign_properties_ref

CREATE TABLE `m_campaign_properties_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `property_key` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_key` (`org_id`,`property_key`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8;


-- end  Schema : m_campaign_properties_ref
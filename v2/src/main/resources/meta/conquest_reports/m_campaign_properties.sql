
-- start  Schema : m_campaign_properties

CREATE TABLE `m_campaign_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `property_value` mediumtext,
  PRIMARY KEY (`id`),
  KEY `key_id` (`key_id`,`org_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


-- end  Schema : m_campaign_properties
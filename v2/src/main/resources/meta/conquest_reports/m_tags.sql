
-- start  Schema : m_tags

CREATE TABLE `m_tags` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `org_id` int(10) NOT NULL,
  `name` varchar(128) NOT NULL,
  `type` enum('BNS_SCHEDULES') NOT NULL,
  `mapped_column_name` varchar(1024) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `is_configurable` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id_is_configurable` (`org_id`,`is_configurable`),
  KEY `tag_name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=357 DEFAULT CHARSET=utf8;


-- end  Schema : m_tags
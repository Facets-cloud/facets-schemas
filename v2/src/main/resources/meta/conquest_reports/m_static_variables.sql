
-- start  Schema : m_static_variables

CREATE TABLE `m_static_variables` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `label` varchar(128) NOT NULL,
  `description` mediumtext NOT NULL,
  `datatype` enum('String','Number','StringList','NumberList') NOT NULL,
  `default_value` varchar(1024) NOT NULL,
  `is_enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `IX_Enabled` (`is_enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- end  Schema : m_static_variables
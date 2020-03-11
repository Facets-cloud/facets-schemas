
-- start  Schema : m_workflows

CREATE TABLE `m_workflows` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `w_name` varchar(256) DEFAULT NULL,
  `w_desc` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_workflows
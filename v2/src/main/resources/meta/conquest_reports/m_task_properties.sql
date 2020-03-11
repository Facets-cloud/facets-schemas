
-- start  Schema : m_task_properties

CREATE TABLE `m_task_properties` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` int(10) unsigned NOT NULL,
  `p_key` varchar(256) NOT NULL,
  `p_default_value` mediumtext NOT NULL,
  `p_value` mediumtext,
  PRIMARY KEY (`id`),
  KEY `task_id_index` (`task_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_task_properties
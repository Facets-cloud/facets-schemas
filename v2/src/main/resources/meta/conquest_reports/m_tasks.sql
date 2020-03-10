
-- start  Schema : m_tasks

CREATE TABLE `m_tasks` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wf_id` int(10) unsigned NOT NULL,
  `t_name` varchar(256) DEFAULT NULL,
  `t_desc` mediumtext,
  `seq_num` int(11) NOT NULL,
  `t_class` varchar(256) NOT NULL,
  `is_temp` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `wf_id_index` (`wf_id`)
) ENGINE=MyISAM AUTO_INCREMENT=857173 DEFAULT CHARSET=utf8;


-- end  Schema : m_tasks
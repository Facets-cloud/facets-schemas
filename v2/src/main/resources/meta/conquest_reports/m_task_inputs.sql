
-- start  Schema : m_task_inputs

CREATE TABLE `m_task_inputs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `i_name` varchar(256) NOT NULL,
  `i_type` varchar(256) NOT NULL,
  `task_id` int(10) unsigned NOT NULL,
  `t_name` varchar(255) NOT NULL,
  `t_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `task_id_index` (`task_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- end  Schema : m_task_inputs
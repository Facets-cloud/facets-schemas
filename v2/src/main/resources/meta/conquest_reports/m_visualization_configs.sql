
-- start  Schema : m_visualization_configs

CREATE TABLE `m_visualization_configs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_id` int(11) NOT NULL,
  `output_name` varchar(255) NOT NULL,
  `json_value` text,
  PRIMARY KEY (`id`),
  KEY `schedule_id` (`schedule_id`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;


-- end  Schema : m_visualization_configs

-- start  Schema : m_output_dimensions

CREATE TABLE `m_output_dimensions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `output_id` int(10) unsigned NOT NULL,
  `d_name` varchar(256) NOT NULL,
  `d_is_col_dimension` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `output_id_index` (`output_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1624 DEFAULT CHARSET=utf8;


-- end  Schema : m_output_dimensions
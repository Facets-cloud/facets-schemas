
-- start  Schema : m_stage_inputs

CREATE TABLE `m_stage_inputs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `stage_id` int(10) unsigned NOT NULL,
  `i_name` varchar(256) NOT NULL,
  `i_display_name` varchar(256) DEFAULT NULL,
  `i_desc` mediumtext,
  `i_query` mediumtext,
  `i_is_collection` tinyint(1) DEFAULT NULL,
  `i_data_type` varchar(256) DEFAULT NULL,
  `i_input_type` varchar(255) DEFAULT NULL,
  `i_is_hidden` tinyint(1) DEFAULT '0',
  `i_is_currency` tinyint(1) DEFAULT '0',
  `i_index` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `stage_id_index` (`stage_id`)
) ENGINE=MyISAM AUTO_INCREMENT=649378 DEFAULT CHARSET=utf8;


-- end  Schema : m_stage_inputs
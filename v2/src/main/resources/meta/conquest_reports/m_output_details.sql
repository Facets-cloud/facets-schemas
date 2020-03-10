
-- start  Schema : m_output_details

CREATE TABLE `m_output_details` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `output_id` int(10) unsigned NOT NULL,
  `v_name` varchar(256) NOT NULL,
  `v_type` varchar(256) NOT NULL,
  `v_display_name` varchar(256) DEFAULT NULL,
  `v_desc` mediumtext,
  `dimension_id` int(10) unsigned DEFAULT NULL,
  `v_dimension_seq_num` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `output_id_index` (`output_id`),
  KEY `dimension_id_index` (`dimension_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1670 DEFAULT CHARSET=utf8;


-- end  Schema : m_output_details
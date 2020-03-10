
-- start  Schema : dsl_source_connection_info

CREATE TABLE `dsl_source_connection_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `has_write_access` tinyint(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_login` varchar(255) NOT NULL,
  `dsl_source_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `dsl_source_id_index` (`dsl_source_id`),
  KEY `FK30D960D19DC1375` (`dsl_source_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


-- end  Schema : dsl_source_connection_info
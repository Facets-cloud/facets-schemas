
-- start  Schema : shard_connection_info

CREATE TABLE `shard_connection_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `has_write_access` tinyint(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_login` varchar(255) NOT NULL,
  `shard_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9C11630EBD4DA97D` (`shard_id`),
  KEY `FK9C11630ED2CE9A7A` (`shard_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;


-- end  Schema : shard_connection_info
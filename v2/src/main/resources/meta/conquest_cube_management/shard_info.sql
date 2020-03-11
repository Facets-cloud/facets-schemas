
-- start  Schema : shard_info

CREATE TABLE `shard_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `request_load` bigint(20) NOT NULL,
  `request_load_factor` double NOT NULL,
  `data_load` bigint(20) NOT NULL,
  `data_load_factor` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;


-- end  Schema : shard_info
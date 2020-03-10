
-- start  Schema : org_cubes_info

CREATE TABLE `org_cubes_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) DEFAULT NULL,
  `primary_shard_info_id` int(11) DEFAULT NULL,
  `failover_shard_info_id` int(11) DEFAULT NULL,
  `db_name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`),
  KEY `FK273AA5AAC46363BC` (`failover_shard_info_id`),
  KEY `FK273AA5AA661800CC` (`primary_shard_info_id`)
) ENGINE=MyISAM AUTO_INCREMENT=919 DEFAULT CHARSET=latin1;


-- end  Schema : org_cubes_info
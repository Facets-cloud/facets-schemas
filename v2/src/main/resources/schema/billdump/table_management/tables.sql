
-- start  Schema : tables

CREATE TABLE `tables` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `type` enum('bar','poolside','normal') NOT NULL DEFAULT 'normal',
  `status` enum('free','reserved') NOT NULL DEFAULT 'free',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `details` blob,
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `unique_index` (`name`,`store_id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;


-- end  Schema : tables
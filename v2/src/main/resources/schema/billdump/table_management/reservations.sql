
-- start  Schema : reservations

CREATE TABLE `reservations` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) unsigned NOT NULL,
  `customer_id` int(11) unsigned NOT NULL,
  `mobile` varchar(25) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `type` enum('advance','walk_in') NOT NULL DEFAULT 'walk_in',
  `status` enum('booked','seated','no_show','finished','cancelled','waiting') NOT NULL,
  `store_id` int(11) NOT NULL,
  `time` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `people_count` int(5) NOT NULL,
  `external_id` varchar(50) DEFAULT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;


-- end  Schema : reservations
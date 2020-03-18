
-- start  Schema : reservation_table_mapping

CREATE TABLE `reservation_table_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `reservation_id` int(11) NOT NULL,
  `table_id` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `CK_Per_reservation_Table_Unique` (`reservation_id`,`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;


-- end  Schema : reservation_table_mapping

-- start  Schema : scd_data_locations

CREATE TABLE `scd_data_locations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `added_on` timestamp NULL DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `dim_column_id` int(11) NOT NULL,
  `dim_table_id` int(11) NOT NULL,
  `table_name` varchar(100) DEFAULT NULL,
  `create_table_schema` text,
  `path` text,
  `type` enum('VERSION','COMPACTED') DEFAULT 'VERSION',
  `is_valid` tinyint(1) DEFAULT '1',
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `name_idx` (`table_name`)
) ENGINE=InnoDB AUTO_INCREMENT=146300 DEFAULT CHARSET=latin1;


-- end  Schema : scd_data_locations
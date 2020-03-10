
-- start  Schema : data_location_dimension_attr

CREATE TABLE `data_location_dimension_attr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `run_id` int(11) NOT NULL,
  `run_date` datetime NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `table_id` int(11) NOT NULL,
  `column_id` int(11) NOT NULL,
  `table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_table_schema` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `path` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `table_data_location` (`org_id`,`table_id`,`column_id`,`run_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14043 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : data_location_dimension_attr

-- start  Schema : data_location_intermediate

CREATE TABLE `data_location_intermediate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `run_id` int(11) NOT NULL,
  `run_date` datetime NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `db_name` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_table_schema` blob NOT NULL,
  `path` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `table_location` (`org_id`,`db_name`,`table_name`,`run_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3628241 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : data_location_intermediate
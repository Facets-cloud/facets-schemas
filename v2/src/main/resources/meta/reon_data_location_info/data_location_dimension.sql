
-- start  Schema : data_location_dimension

CREATE TABLE `data_location_dimension` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `run_id` int(11) NOT NULL,
  `version_id` int(11) NOT NULL DEFAULT '1',
  `run_date` datetime NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `table_id` int(11) NOT NULL,
  `table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_table_schema` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `path` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `table_location` (`org_id`,`table_id`,`run_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13251871 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : data_location_dimension

-- start  Schema : data_location_source

CREATE TABLE `data_location_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `run_id` int(11) NOT NULL,
  `run_date` datetime NOT NULL,
  `org_id` int(11) DEFAULT '-1',
  `table_id` int(11) NOT NULL DEFAULT '-1',
  `table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `db_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `endpoint_id` int(11) NOT NULL DEFAULT '-1',
  `create_table_schema` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `path` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `table_location` (`org_id`,`table_id`,`run_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62234 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : data_location_source
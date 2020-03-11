
-- start  Schema : data_schema_storage

CREATE TABLE `data_schema_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `added_date` datetime NOT NULL,
  `name_space` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_schema` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `properties` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `table_location` (`name_space`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : data_schema_storage
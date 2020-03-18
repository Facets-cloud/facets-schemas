
-- start  Schema : rolling_table_mapping

CREATE TABLE `rolling_table_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `original_table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rolling_table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `is_archived` tinyint(1) DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_orig_table_name` (`original_table_name`),
  KEY `auto_time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : rolling_table_mapping
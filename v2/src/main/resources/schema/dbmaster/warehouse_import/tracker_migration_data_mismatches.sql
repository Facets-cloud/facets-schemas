
-- start  Schema : tracker_migration_data_mismatches

CREATE TABLE `tracker_migration_data_mismatches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `tracker_id` int(11) NOT NULL,
  `points_category_id` int(11) NOT NULL,
  `ref_id` bigint(20) DEFAULT NULL,
  `ref_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `execution_date` datetime NOT NULL,
  `tracker_data_id` int(11) DEFAULT NULL,
  `points_data_id` int(11) DEFAULT NULL,
  `tracked_value` bigint(10) DEFAULT NULL,
  `tracked_date` datetime DEFAULT NULL,
  `points_value` bigint(10) DEFAULT NULL,
  `points_date` datetime DEFAULT NULL,
  `mismatched_fields` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `index_org_trk_custm` (`org_id`,`tracker_id`,`customer_id`),
  KEY `index_org_cat_custm` (`org_id`,`points_category_id`,`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracker_migration_data_mismatches
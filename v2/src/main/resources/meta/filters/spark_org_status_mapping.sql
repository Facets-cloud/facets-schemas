
-- start  Schema : spark_org_status_mapping

CREATE TABLE `spark_org_status_mapping` (
  `org_id` int(11) NOT NULL,
  `is_spark_enabled` tinyint(4) DEFAULT '0',
  `is_old_filter_enabled` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : spark_org_status_mapping
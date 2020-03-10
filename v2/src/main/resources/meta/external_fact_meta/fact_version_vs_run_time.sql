
-- start  Schema : fact_version_vs_run_time

CREATE TABLE `fact_version_vs_run_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fact_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` bigint(20) NOT NULL,
  `run_id` int(11) NOT NULL,
  `run_time` timestamp NULL DEFAULT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : fact_version_vs_run_time
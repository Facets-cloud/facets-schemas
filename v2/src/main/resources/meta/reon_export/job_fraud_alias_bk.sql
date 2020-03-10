
-- start  Schema : job_fraud_alias_bk

CREATE TABLE `job_fraud_alias_bk` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `org_id` int(11) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `fraud_kpi` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `alias_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : job_fraud_alias_bk

-- start  Schema : fraud_templates_bkp

CREATE TABLE `fraud_templates_bkp` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `org_id` int(11) NOT NULL,
  `params` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `fact_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `template_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : fraud_templates_bkp
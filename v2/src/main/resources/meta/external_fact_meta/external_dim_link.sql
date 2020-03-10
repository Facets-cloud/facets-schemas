
-- start  Schema : external_dim_link

CREATE TABLE `external_dim_link` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dim_link_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `display_name` text COLLATE utf8mb4_unicode_ci,
  `dim_table_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `version` bigint(20) NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `description` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : external_dim_link
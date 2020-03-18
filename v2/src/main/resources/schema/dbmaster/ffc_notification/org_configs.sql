
-- start  Schema : org_configs

CREATE TABLE `org_configs` (
  `id` int(50) NOT NULL,
  `org_id` int(30) NOT NULL,
  `is_ffc_enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : org_configs
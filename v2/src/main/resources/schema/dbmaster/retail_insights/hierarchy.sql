
-- start  Schema : hierarchy

CREATE TABLE `hierarchy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `till_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `store_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `zone_id` int(11) NOT NULL,
  `zone_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_entity_id` int(11) NOT NULL,
  `hierarchy_type` enum('ZONE','CONCEPT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `level` int(11) NOT NULL,
  `is_ffc_enabled` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `till_id` (`till_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : hierarchy
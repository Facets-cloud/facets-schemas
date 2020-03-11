
-- start  Schema : filter

CREATE TABLE `filter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `resource_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Auto',
  `type` enum('LOYALTY','NON_LOYALTY','ALL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'LOYALTY',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `supported_orgs` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '-1',
  `last_updated_on` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `desc_idx` (`description`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : filter
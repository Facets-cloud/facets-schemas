
-- start  Schema : zones

CREATE TABLE `zones` (
  `id` int(11) NOT NULL,
  `level` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `reporting_email` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Denormalization for easy view creation',
  `reporting_mobile` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Denormalization',
  `org_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : zones
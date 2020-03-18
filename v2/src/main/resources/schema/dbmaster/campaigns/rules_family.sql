
-- start  Schema : rules_family

CREATE TABLE `rules_family` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` bigint(20) NOT NULL DEFAULT '1',
  `created_time` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `expiry_date` datetime NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : rules_family
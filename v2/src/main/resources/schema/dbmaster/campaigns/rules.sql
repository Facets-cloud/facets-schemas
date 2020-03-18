
-- start  Schema : rules

CREATE TABLE `rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_text` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `created_time` datetime NOT NULL,
  `created_by` int(11) DEFAULT NULL,
  `is_valid` tinyint(1) DEFAULT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `rules_family_id` int(11) NOT NULL,
  `weight` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : rules
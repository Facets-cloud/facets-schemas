
-- start  Schema : config_keys

CREATE TABLE `config_keys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scopes` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_type` enum('STRING','NUMERIC','BOOL','LIST','MAP','RANGE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `default_value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `label` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `security_group` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `modules` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assoc_keys` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `regex` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `custom_check` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` int(11) NOT NULL,
  `added_on` datetime NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `parent_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `key_name` (`name`),
  KEY `validity` (`is_valid`,`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1404 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : config_keys
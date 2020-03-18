
-- start  Schema : config_key_values

CREATE TABLE `config_key_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `entity_id` int(11) NOT NULL,
  `scope` enum('STORE','ZONE','ORG','TILL','STR_SERVER','OU') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_by` int(11) NOT NULL COMMENT 'user who updated the key',
  `updated_on` datetime NOT NULL COMMENT 'time when the key is updated',
  `is_valid` tinyint(1) NOT NULL,
  `parent_id` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`),
  KEY `entity_id` (`entity_id`),
  KEY `parent_id` (`parent_id`),
  KEY `keys_names` (`key_id`,`org_id`,`is_valid`,`scope`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : config_key_values
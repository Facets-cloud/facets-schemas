
-- start  Schema : mobile_trigger_config_keys

CREATE TABLE `mobile_trigger_config_keys` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `action_id` bigint(20) NOT NULL,
  `default_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` enum('string','integer','boolean','text','enum') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'string',
  `label` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `possible_values` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `action_id_name_idx` (`action_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mobile_trigger_config_keys

-- start  Schema : mobile_trigger_config_key_values

CREATE TABLE `mobile_trigger_config_key_values` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_id` bigint(20) NOT NULL,
  `trigger_id` bigint(20) NOT NULL,
  `value` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` datetime NOT NULL,
  `added_by` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `is_valid` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_trigger_key_idx` (`org_id`,`trigger_id`,`key_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mobile_trigger_config_key_values
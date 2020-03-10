
-- start  Schema : import_conf_keys

CREATE TABLE `import_conf_keys` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'configuration key id',
  `profile_id` int(11) NOT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `label` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ui_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `values` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `default_value` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'validation',
  `is_mandatory` tinyint(4) DEFAULT '0',
  `order_id` int(10) DEFAULT '30',
  `parent_conf_key_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `profile_id` (`profile_id`,`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Configuration for each profile in import.';


-- end  Schema : import_conf_keys
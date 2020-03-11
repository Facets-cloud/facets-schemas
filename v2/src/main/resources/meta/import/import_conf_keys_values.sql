
-- start  Schema : import_conf_keys_values

CREATE TABLE `import_conf_keys_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_id` int(11) NOT NULL COMMENT 'config key id',
  `template_id` int(11) NOT NULL COMMENT 'Template id ',
  `value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'config key values',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Key_template_index` (`key_id`,`template_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Table that stores the import config key values for each temp';


-- end  Schema : import_conf_keys_values
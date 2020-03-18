
-- start  Schema : system_rules_scopes_property_values

CREATE TABLE `system_rules_scopes_property_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL,
  `system_rule_id` int(11) NOT NULL,
  `scope_id` int(11) NOT NULL,
  `scope_property_id` int(11) NOT NULL,
  `value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The values for the associated property',
  PRIMARY KEY (`id`,`org_id`),
  KEY `program_id` (`program_id`,`system_rule_id`,`scope_id`,`scope_property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : system_rules_scopes_property_values
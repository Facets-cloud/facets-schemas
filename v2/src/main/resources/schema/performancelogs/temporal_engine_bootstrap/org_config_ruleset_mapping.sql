
-- start  Schema : org_config_ruleset_mapping

CREATE TABLE `org_config_ruleset_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `org_config_id` int(11) NOT NULL COMMENT 'temporal engine org_config id',
  `ruleset_info_id` int(11) NOT NULL COMMENT 'Rule set id',
  `last_modified_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `program_id` (`org_id`,`org_config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=760 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_config_ruleset_mapping
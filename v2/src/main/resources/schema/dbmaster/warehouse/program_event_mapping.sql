
-- start  Schema : program_event_mapping

CREATE TABLE `program_event_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL,
  `event_type_id` int(11) NOT NULL,
  `start_rule_identifier` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Identifier of the rule in the ruleset  for the program',
  `last_modified_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`program_id`,`event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : program_event_mapping
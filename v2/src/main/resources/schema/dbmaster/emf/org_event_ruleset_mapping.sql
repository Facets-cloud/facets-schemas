
-- start  Schema : org_event_ruleset_mapping

CREATE TABLE `org_event_ruleset_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `event_type_id` int(11) NOT NULL,
  `rule_set_info_id` int(11) NOT NULL COMMENT 'Rule set id',
  `last_modified_on` datetime NOT NULL,
  `event_sub_type_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'event type is same for all generic events, need a sub type to distinguish',
  PRIMARY KEY (`id`,`org_id`),
  KEY `program_id` (`org_id`,`event_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_event_ruleset_mapping
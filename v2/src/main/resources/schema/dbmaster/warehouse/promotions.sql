
-- start  Schema : promotions

CREATE TABLE `promotions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'auto generated promotion id',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `program_id` int(11) NOT NULL COMMENT 'the program to which the promotion belongs',
  `org_id` int(11) NOT NULL COMMENT 'organization id owning the promotion',
  `event_type_id` int(11) NOT NULL COMMENT 'type of the event under which the promotion is valid',
  `is_active` tinyint(1) NOT NULL COMMENT 'True if the promotion is active',
  `is_exclusive` tinyint(1) NOT NULL COMMENT 'does the promotion belong to exclusive group or not',
  `is_system_generated` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Whether the promotion is system generated or not',
  `type` enum('BILL','LINEITEM','CUSTOMER','RETURN') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `promotion_evaluation_type` enum('BEFORE_EVENT','AFTER_EVENT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'AFTER_EVENT',
  `source_type` enum('UI','IMPORT','GOODWILL','CAMPAIGN','POINTS_TRANSFER','IMPORT_API') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source_id` int(11) NOT NULL DEFAULT '-1',
  `start_date` datetime NOT NULL COMMENT 'Start of the campaign',
  `end_date` datetime NOT NULL COMMENT 'Date when the promotion was closed',
  `start_rule_identifier` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ref to the ruleset which identifies if a tx/item is part of the promotion',
  `created_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `event_sub_type_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'event type is same for all generic events, need a sub type to distinguish',
  `points_per_promotion_limit` int(11) NOT NULL DEFAULT '0' COMMENT 'cap on points per promotion',
  `events_per_member_limit` int(11) NOT NULL DEFAULT '0' COMMENT 'cap on events for which promotion can be applied',
  `points_per_member_limit` int(11) NOT NULL DEFAULT '0' COMMENT 'cap on points per member in a promotion',
  `points_per_event_limit` int(11) NOT NULL DEFAULT '0' COMMENT 'cap on points per promotion in a single event',
  PRIMARY KEY (`id`,`org_id`),
  KEY `program_id` (`program_id`,`event_type_id`),
  KEY `auto_update_time` (`auto_update_time`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : promotions
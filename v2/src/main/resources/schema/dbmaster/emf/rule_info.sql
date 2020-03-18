
-- start  Schema : rule_info

CREATE TABLE `rule_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `ruleset_id` int(11) DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Rule Name',
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `expression` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `expression_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `sequence_id` int(11) NOT NULL,
  `json_type` enum('JNODE','DVSNODE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'JNODE',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Whether rule is active or not',
  `priority` smallint(6) NOT NULL DEFAULT '1' COMMENT 'Defines the priority of the rule, the bigger number denominates higher priority.',
  `rule_scope` enum('SERVER','CLIENT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SERVER',
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `created_by` int(11) NOT NULL DEFAULT '-1',
  `created_on` datetime NOT NULL,
  `last_modified` datetime NOT NULL,
  `last_modified_by` int(11) NOT NULL COMMENT 'User ID of user who modified this rule last.',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`ruleset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : rule_info
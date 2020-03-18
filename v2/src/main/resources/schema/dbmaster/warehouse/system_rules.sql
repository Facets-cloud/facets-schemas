
-- start  Schema : system_rules

CREATE TABLE `system_rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'auto generated rule id',
  `program_id` int(11) NOT NULL COMMENT 'The program to which this rule belongs',
  `org_id` int(11) NOT NULL COMMENT 'The organization id if the rule is defined at organization level ',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `scopes_templates_xml` longtext COLLATE utf8mb4_unicode_ci COMMENT 'scopes template xml',
  `action_templates_xml` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'all the action templates supported',
  `rulesets_xml` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'contents of the rule',
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`program_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : system_rules

-- start  Schema : rule_info_bkp

CREATE TABLE `rule_info_bkp` (
  `id` int(11) NOT NULL DEFAULT '0',
  `org_id` int(11) NOT NULL,
  `ruleset_id` int(11) DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Rule Name',
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `expression` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `expression_json` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `sequence_id` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Whether rule is active or not',
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `created_by` int(11) NOT NULL DEFAULT '-1',
  `created_on` datetime NOT NULL,
  `last_modified` datetime NOT NULL,
  `last_modified_by` int(11) NOT NULL COMMENT 'User ID of user who modified this rule last.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : rule_info_bkp
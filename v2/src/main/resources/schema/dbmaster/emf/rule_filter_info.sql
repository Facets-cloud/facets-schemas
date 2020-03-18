
-- start  Schema : rule_filter_info

CREATE TABLE `rule_filter_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `rule_id` int(11) DEFAULT NULL COMMENT 'rule to which filter should be applied to',
  `name` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name of the rule filter',
  `class` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'class of the filter',
  `include` tinyint(1) NOT NULL COMMENT 'Whether to include or exclude this rule filter',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : rule_filter_info
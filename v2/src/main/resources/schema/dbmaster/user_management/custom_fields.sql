
-- start  Schema : custom_fields

CREATE TABLE `custom_fields` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'type of the field',
  `datatype` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data type of the field',
  `label` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `default` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'default values json converted array',
  `phase` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `rule` longtext COLLATE utf8mb4_unicode_ci,
  `server_rule` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `regex` mediumtext COLLATE utf8mb4_unicode_ci,
  `helptext` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error` mediumtext COLLATE utf8mb4_unicode_ci,
  `attrs` longtext COLLATE utf8mb4_unicode_ci COMMENT 'JSON converted',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0',
  `is_compulsory` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Whether the data for the custom field has to be present or not',
  `is_updatable` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'If set, \nthe value can only be set once and read many times',
  `modified_by` bigint(20) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `disable_at_server` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'it is used to disable custom field on serverside menu',
  `ef_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`name`),
  KEY `auto_time_idx` (`last_modified`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : custom_fields
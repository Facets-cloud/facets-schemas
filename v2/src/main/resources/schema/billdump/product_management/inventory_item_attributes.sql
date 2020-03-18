
-- start  Schema : inventory_item_attributes

CREATE TABLE `inventory_item_attributes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `label` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_enum` tinyint(1) NOT NULL,
  `extraction_rule_type` enum('UPLOAD','POS','REGEX','USERDEF') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The allowed extraction rules',
  `extraction_rule_data` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The configuration of the rule as json',
  `type` enum('String','Int','Boolean','Double') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'String' COMMENT 'Datatype of the attribute',
  `is_soft_enum` int(1) NOT NULL DEFAULT '0',
  `use_in_dump` int(1) NOT NULL DEFAULT '1',
  `default_attribute_value_id` int(11) DEFAULT NULL COMMENT 'Default value for an attribute',
  `added_by` bigint(20) NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`name`),
  KEY `org_id_auto_update_timestamp` (`org_id`,`auto_update_timestamp`),
  KEY `auto_update_time_idx` (`auto_update_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=5324 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Information about the different attributes in each item';


-- end  Schema : inventory_item_attributes
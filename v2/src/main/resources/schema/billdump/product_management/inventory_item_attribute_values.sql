
-- start  Schema : inventory_item_attribute_values

CREATE TABLE `inventory_item_attribute_values` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `attribute_id` bigint(20) NOT NULL,
  `value_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'eg: White is Color',
  `value_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'eg: Code for White is 15',
  `added_by` bigint(20) NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_attr_value_idx` (`org_id`,`attribute_id`,`value_name`),
  KEY `org_id_2` (`org_id`,`value_name`),
  KEY `org_id_auto_update_timestamp` (`org_id`,`auto_update_timestamp`),
  KEY `auto_update_time_idx` (`auto_update_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=276328206 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='All the possible values for an attribute';


-- end  Schema : inventory_item_attribute_values
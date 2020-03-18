
-- start  Schema : inventory_item_attribute_values

CREATE TABLE `inventory_item_attribute_values` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `attribute_id` bigint(20) NOT NULL,
  `value_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'eg: White is Color',
  `value_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'eg: Code for White is 15',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_attr_value_idx` (`org_id`,`attribute_id`,`value_name`),
  KEY `org_id_2` (`org_id`,`value_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='All the possible values for an attribute';


-- end  Schema : inventory_item_attribute_values
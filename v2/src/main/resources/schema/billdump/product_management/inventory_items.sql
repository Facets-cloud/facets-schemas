
-- start  Schema : inventory_items

CREATE TABLE `inventory_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `item_id` bigint(20) NOT NULL,
  `attribute_id` bigint(20) NOT NULL,
  `attribute_value_id` bigint(20) NOT NULL,
  `value` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_item_attribute_idx` (`org_id`,`item_id`,`attribute_id`),
  KEY `org_id_2` (`org_id`,`attribute_id`,`attribute_value_id`),
  KEY `org_id_value` (`org_id`,`attribute_id`,`value`),
  KEY `org_id_auto_update_timestamp` (`org_id`,`auto_update_timestamp`),
  KEY `auto_update_time_idx` (`auto_update_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=2467708790 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='item level details';


-- end  Schema : inventory_items

-- start  Schema : inventory_items

CREATE TABLE `inventory_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `item_id` bigint(20) NOT NULL,
  `attribute_id` bigint(20) NOT NULL,
  `attribute_value_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_item_attribute_idx` (`org_id`,`item_id`,`attribute_id`),
  KEY `org_id` (`org_id`,`item_id`),
  KEY `org_id_2` (`org_id`,`attribute_id`,`attribute_value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='item level details';


-- end  Schema : inventory_items
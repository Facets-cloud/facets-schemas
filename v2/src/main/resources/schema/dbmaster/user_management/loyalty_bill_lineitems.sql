
-- start  Schema : loyalty_bill_lineitems

CREATE TABLE `loyalty_bill_lineitems` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loyalty_log_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `serial` int(11) NOT NULL COMMENT 'Serial number of the bill item - used for idempotency - starting from 1',
  `item_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `rate` double NOT NULL COMMENT 'Rate of the item',
  `qty` double NOT NULL COMMENT 'Qty (can be float)',
  `value` double NOT NULL COMMENT 'Value before discount',
  `discount_value` double NOT NULL COMMENT 'Total discount given on this item',
  `amount` double NOT NULL COMMENT 'Amount of this item',
  `store_id` bigint(20) NOT NULL COMMENT 'Store-ID that is making the entry',
  `inventory_item_id` bigint(20) DEFAULT NULL COMMENT 'The item id if present in the inventory',
  `outlier_status` enum('INTERNAL','NORMAL','INVALID','OUTLIER','FAILED','OTHERS') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL',
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `mapped_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type_id` mediumint(9) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`loyalty_log_id`),
  KEY `org_id_2` (`org_id`,`inventory_item_id`),
  KEY `org_ll_sku_idx` (`org_id`,`item_code`,`loyalty_log_id`) USING BTREE,
  KEY `org_id_updated_on` (`org_id`,`updated_on`),
  KEY `org_id_3` (`org_id`,`user_id`) USING BTREE,
  KEY `auto_time_idx` (`updated_on`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty_bill_lineitems
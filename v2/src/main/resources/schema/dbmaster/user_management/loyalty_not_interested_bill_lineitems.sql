
-- start  Schema : loyalty_not_interested_bill_lineitems

CREATE TABLE `loyalty_not_interested_bill_lineitems` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `not_interested_bill_id` bigint(20) NOT NULL,
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
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `outlier_status` enum('INTERNAL','NORMAL','INVALID','OUTLIER','FAILED','OTHERS') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`not_interested_bill_id`),
  KEY `org_id_2` (`org_id`,`inventory_item_id`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty_not_interested_bill_lineitems
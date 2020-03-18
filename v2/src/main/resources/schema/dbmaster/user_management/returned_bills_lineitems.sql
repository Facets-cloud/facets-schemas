
-- start  Schema : returned_bills_lineitems

CREATE TABLE `returned_bills_lineitems` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `return_bill_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `serial` int(11) NOT NULL COMMENT 'Serial number of the bill item - used for idempotency - starting from 1',
  `item_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rate` double NOT NULL COMMENT 'Rate of the item',
  `qty` double NOT NULL COMMENT 'Qty (can be float)',
  `value` double NOT NULL COMMENT 'Value before discount',
  `discount_value` double NOT NULL COMMENT 'Total discount given on this item',
  `amount` double NOT NULL COMMENT 'Amount of this item',
  `lbl_id` int(11) NOT NULL,
  `loyalty_log_id` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`return_bill_id`),
  KEY `org_id_2` (`org_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : returned_bills_lineitems
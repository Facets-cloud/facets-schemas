
-- start  Schema : not_interested_return_bill_lineitems

CREATE TABLE `not_interested_return_bill_lineitems` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `not_interested_return_bill_id` bigint(20) NOT NULL,
  `serial` int(11) NOT NULL,
  `item_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` tinytext COLLATE utf8mb4_unicode_ci,
  `rate` double NOT NULL,
  `qty` double NOT NULL,
  `value` double NOT NULL,
  `discount_value` double DEFAULT NULL,
  `amount` double NOT NULL,
  `lineitem_id` int(11) DEFAULT NULL,
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`not_interested_return_bill_id`),
  KEY `org_time_idx` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : not_interested_return_bill_lineitems
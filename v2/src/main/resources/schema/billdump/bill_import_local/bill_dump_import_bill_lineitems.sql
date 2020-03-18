
-- start  Schema : bill_dump_import_bill_lineitems

CREATE TABLE `bill_dump_import_bill_lineitems` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `bill_dump_import_bills_id` bigint(20) NOT NULL COMMENT 'Reference to id column of the bill dump import bills table',
  `serial` int(11) NOT NULL,
  `item_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `mrp` double NOT NULL,
  `discount_value` double NOT NULL,
  `amount` double NOT NULL,
  `qty` int(11) NOT NULL,
  `entered_by` bigint(20) DEFAULT NULL,
  `processed` datetime DEFAULT NULL,
  `loyalty_bill_lineitems_id_ref` bigint(20) DEFAULT NULL,
  `unique_id` bigint(20) NOT NULL DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `lineitem` (`org_id`,`bill_dump_import_bills_id`),
  KEY `bill_org_entered_unique_idx` (`bill_dump_import_bills_id`,`org_id`,`entered_by`,`unique_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`),
  KEY `auto_update_time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=413654 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bill_dump_import_bill_lineitems
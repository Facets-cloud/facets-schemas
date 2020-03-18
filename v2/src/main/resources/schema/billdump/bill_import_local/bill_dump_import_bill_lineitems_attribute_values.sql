
-- start  Schema : bill_dump_import_bill_lineitems_attribute_values

CREATE TABLE `bill_dump_import_bill_lineitems_attribute_values` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `bill_dump_import_bill_lineitem_id` bigint(20) NOT NULL,
  `bill_dump_import_attribute_id` int(11) NOT NULL,
  `bill_dump_import_attribute_value` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `bill_dump_import_attribute_name` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`bill_dump_import_bill_lineitem_id`,`bill_dump_import_attribute_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`),
  KEY `org_attribute_idx` (`org_id`,`bill_dump_import_attribute_id`),
  KEY `auto_update_time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bill_dump_import_bill_lineitems_attribute_values
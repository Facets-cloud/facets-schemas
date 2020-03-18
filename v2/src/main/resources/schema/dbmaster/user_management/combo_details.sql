
-- start  Schema : combo_details

CREATE TABLE `combo_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `lineitem_id` bigint(20) NOT NULL,
  `type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_lineitem_id` bigint(20) NOT NULL,
  `parent_sku_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `txn_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `txn_id` bigint(20) DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `txn_type_id_unq` (`txn_type`,`org_id`,`lineitem_id`),
  KEY `txn_type_parent_id` (`org_id`,`txn_type`,`parent_lineitem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : combo_details
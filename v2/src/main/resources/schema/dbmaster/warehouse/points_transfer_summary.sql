
-- start  Schema : points_transfer_summary

CREATE TABLE `points_transfer_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'points transfer summary id',
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL COMMENT 'program id for which the transaction belongs to',
  `point_category_id` int(11) NOT NULL,
  `points_credited` decimal(15,3) NOT NULL DEFAULT '0.000',
  `points_deducted` decimal(15,3) NOT NULL DEFAULT '0.000',
  `transfer_type` enum('TRANSFER','GROUP_TRANSFER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TRANSFER',
  `from_customer_id` bigint(20) NOT NULL COMMENT 'customer id of the customer transferring points',
  `to_customer_id` bigint(20) NOT NULL COMMENT 'customer id of the customer to whom points are being transferred',
  `from_customer_group` int(11) NOT NULL,
  `to_customer_group` int(11) NOT NULL,
  `till_id` bigint(20) DEFAULT NULL,
  `transfer_notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `transferred_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `pa_id` bigint(20) NOT NULL,
  `pa_type` enum('POINT_AWARDED','POINT_AWARDED_BILL_PROMOTION','POINT_AWARDED_LINEITEM','POINT_AWARDED_LINEITEM_PROMOTION','POINT_AWARDED_CUSTOMER_PROMOTION') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `customer_index` (`org_id`,`program_id`,`from_customer_id`,`to_customer_id`,`transferred_on`),
  KEY `org_program_from_customer_idx` (`org_id`,`program_id`,`from_customer_id`),
  KEY `org_program_to_customer_idx` (`org_id`,`program_id`,`to_customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : points_transfer_summary
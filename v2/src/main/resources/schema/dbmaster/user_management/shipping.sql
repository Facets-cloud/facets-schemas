
-- start  Schema : shipping

CREATE TABLE `shipping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `bill_id` int(11) NOT NULL,
  `shipping_external_id` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `courier_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tracking_number` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `invoice_number` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipping_cost` double DEFAULT NULL,
  `shipment_type_id` int(11) DEFAULT NULL,
  `source_location_id` int(11) DEFAULT NULL,
  `shipping_contact_id` int(11) DEFAULT NULL,
  `verification_method` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comments` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount_to_collect` double DEFAULT NULL,
  `pickup_by` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pickup_time` datetime DEFAULT NULL,
  `expected_delivery_date` datetime DEFAULT NULL,
  `delivery_mode_id` int(11) DEFAULT NULL,
  `created_on` datetime NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `expected_delivery_time` datetime DEFAULT NULL,
  `last_updated_by` int(11) NOT NULL,
  `created_by` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bill_type` enum('loyalty','non_loyalty') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'loyalty',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `shipping_ext_id` (`org_id`,`shipping_external_id`) USING BTREE,
  KEY `bill_id` (`org_id`,`bill_id`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : shipping

-- start  Schema : merge_customer_summary

CREATE TABLE `merge_customer_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `from_customer_id` int(11) NOT NULL,
  `to_customer_id` int(11) NOT NULL,
  `merge_request_id` int(11) NOT NULL,
  `merged_by` int(11) NOT NULL,
  `status` enum('SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `error_message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `request_id` (`org_id`,`merge_request_id`),
  KEY `customer_id` (`org_id`,`from_customer_id`,`to_customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=624669 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED;


-- end  Schema : merge_customer_summary
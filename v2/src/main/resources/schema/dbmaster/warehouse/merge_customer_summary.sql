
-- start  Schema : merge_customer_summary

CREATE TABLE `merge_customer_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `program_id` int(11) NOT NULL,
  `from_customer_id` int(11) NOT NULL,
  `to_customer_id` int(11) NOT NULL,
  `merged_date` datetime NOT NULL,
  `till_id` int(11) NOT NULL,
  `request_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `error_message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_update_time` (`auto_update_time`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `customer_id` (`org_id`,`program_id`,`from_customer_id`,`to_customer_id`) USING BTREE,
  KEY `request_id` (`program_id`,`request_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : merge_customer_summary
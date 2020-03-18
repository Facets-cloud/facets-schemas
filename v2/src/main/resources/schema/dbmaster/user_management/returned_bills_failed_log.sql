
-- start  Schema : returned_bills_failed_log

CREATE TABLE `returned_bills_failed_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `loyalty_log_id` bigint(20) DEFAULT NULL,
  `parent_loyalty_log_id` bigint(20) DEFAULT NULL,
  `reason` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lineitem_info` longtext COLLATE utf8mb4_unicode_ci COMMENT 'json encoded info of the return line items',
  `entered_by` int(11) NOT NULL,
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_user_idx` (`org_id`,`user_id`),
  KEY `org_added_on` (`org_id`,`added_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : returned_bills_failed_log
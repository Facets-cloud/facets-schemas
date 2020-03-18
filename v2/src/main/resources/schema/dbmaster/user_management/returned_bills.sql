
-- start  Schema : returned_bills

CREATE TABLE `returned_bills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `bill_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `credit_note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount` double NOT NULL,
  `points` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `returned_on` datetime NOT NULL,
  `loyalty_log_id` bigint(20) DEFAULT NULL COMMENT 'map to the loyalty log entry',
  `parent_loyalty_log_id` bigint(20) DEFAULT NULL COMMENT 'loyalty_log_id during which the return has happened - For mixed txn',
  `type` enum('FULL','LINE_ITEM','AMOUNT','CANCELLED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_bill_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `returned_bill_date` datetime DEFAULT NULL,
  `outlier_status` enum('NORMAL','INTERNAL','FRAUD','OUTLIER','TEST','FAILED','OTHER','DELETED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`user_id`,`bill_number`),
  KEY `returned_on` (`returned_on`,`org_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`),
  KEY `org_parent_id_idx` (`org_id`,`parent_loyalty_log_id`),
  KEY `org_number` (`org_id`,`bill_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : returned_bills
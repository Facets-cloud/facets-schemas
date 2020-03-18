
-- start  Schema : not_interested_return_bills_failed_log

CREATE TABLE `not_interested_return_bills_failed_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `loyalty_not_interested_bill_id` bigint(20) DEFAULT NULL,
  `parent_loyalty_not_interested_bill_id` bigint(20) DEFAULT NULL,
  `reason` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount` float NOT NULL,
  `date` datetime DEFAULT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `previous_till_id` int(11) NOT NULL,
  `previous_bill_date` datetime NOT NULL,
  `lineitem_info` longtext COLLATE utf8mb4_unicode_ci,
  `entered_by` int(11) NOT NULL,
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_added_on` (`org_id`,`added_on`),
  KEY `idx_not_interested_return_bills_failed_log` (`loyalty_not_interested_bill_id`),
  KEY `idx_not_interested_return_bills_failed_log_0` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : not_interested_return_bills_failed_log
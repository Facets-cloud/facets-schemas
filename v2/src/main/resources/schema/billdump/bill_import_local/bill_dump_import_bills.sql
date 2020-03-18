
-- start  Schema : bill_dump_import_bills

CREATE TABLE `bill_dump_import_bills` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `bill_amount` float NOT NULL,
  `entered_by` bigint(20) DEFAULT NULL,
  `processed` datetime DEFAULT NULL COMMENT 'When was this bill last processed',
  `loyalty_log_id_ref` bigint(20) DEFAULT NULL COMMENT 'Reference to loyalty log id..',
  `external_id_used` mediumtext COLLATE utf8mb4_unicode_ci,
  `customer_int` bigint(20) NOT NULL,
  `customer_varchar` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_flag` enum('VALID','INVALID','OUTLIER','FRAUD') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'VALID',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `bill_number` (`org_id`,`bill_number`),
  KEY `bill_date` (`org_id`,`date`),
  KEY `org_id` (`org_id`),
  KEY `entered_by` (`entered_by`),
  KEY `bill_org_date_entered_idx` (`bill_number`,`org_id`,`date`,`entered_by`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`),
  KEY `auto_update_time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=228338 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bill_dump_import_bills
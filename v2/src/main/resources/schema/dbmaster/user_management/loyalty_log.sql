
-- start  Schema : loyalty_log

CREATE TABLE `loyalty_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loyalty_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `points` int(11) NOT NULL,
  `redeemed` int(11) NOT NULL DEFAULT '0',
  `expired` int(11) NOT NULL DEFAULT '0',
  `date` datetime NOT NULL,
  `notes` longtext COLLATE utf8mb4_unicode_ci,
  `bill_amount` double NOT NULL,
  `entered_by` bigint(20) NOT NULL,
  `outlier_status` enum('NORMAL','INTERNAL','FRAUD','OUTLIER','TEST','FAILED','OTHER','DELETED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL',
  `counter_id` bigint(20) DEFAULT NULL COMMENT 'counter id which caused the entry',
  `raymond_trans` int(11) DEFAULT NULL,
  `bill_gross_amount` double NOT NULL DEFAULT '0' COMMENT 'amount before discount',
  `bill_discount` double NOT NULL DEFAULT '0' COMMENT 'discount given on the bill',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `loyalty_type` enum('loyalty','non_loyalty') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'loyalty',
  `source` enum('instore','e-comm','newsletter') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'instore',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`bill_number`),
  KEY `loyalty_id` (`loyalty_id`),
  KEY `org_id_2` (`org_id`,`user_id`),
  KEY `org_id_3` (`org_id`,`date`),
  KEY `org_id_4` (`org_id`,`entered_by`),
  KEY `org_lid_idx` (`org_id`,`loyalty_id`),
  KEY `org_user_store_bill_idx` (`org_id`,`user_id`,`bill_number`,`entered_by`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty_log
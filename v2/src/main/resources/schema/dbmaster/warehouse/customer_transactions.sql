
-- start  Schema : customer_transactions

CREATE TABLE `customer_transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'program transaction id',
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL COMMENT 'program id for which the transaction belongs to',
  `customer_id` int(11) NOT NULL,
  `bill_id` bigint(20) NOT NULL COMMENT 'loyalty_log_id or the bill id of the transaction',
  `bill_amount` decimal(15,3) NOT NULL DEFAULT '0.000',
  `num_lineitems` int(11) NOT NULL DEFAULT '0',
  `return_amount` decimal(15,3) NOT NULL DEFAULT '0.000',
  `status` enum('NORMAL','INTERNAL','FRAUD','OUTLIER','TEST','FAILED','OTHER','DELETED','RETURNED') COLLATE utf8mb4_unicode_ci DEFAULT 'NORMAL',
  `billing_time` datetime NOT NULL,
  `till_id` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`org_id`,`program_id`,`customer_id`,`bill_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : customer_transactions
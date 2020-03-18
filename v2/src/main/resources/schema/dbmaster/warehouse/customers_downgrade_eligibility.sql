
-- start  Schema : customers_downgrade_eligibility

CREATE TABLE `customers_downgrade_eligibility` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `program_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL COMMENT 'customer id which is to be evaluated for downgrade',
  `current_slab_id` int(11) NOT NULL COMMENT 'slab in which customer is present before downgrade',
  `bill_id` int(10) NOT NULL COMMENT 'bill id of the returned bill',
  `downgrade_status` enum('OPEN','IN_PROGRESS','DOWNGRADED','RETAINED','FAILED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `return_date` datetime NOT NULL COMMENT 'date when return bill event is triggered',
  `scheduled_on` datetime NOT NULL COMMENT 'system date used to evaluate the customers who should be downgraded',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_loyalty_log_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `program_id` (`org_id`,`program_id`,`customer_id`),
  KEY `org_program_scheduled_on_idx` (`org_id`,`program_id`,`scheduled_on`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : customers_downgrade_eligibility
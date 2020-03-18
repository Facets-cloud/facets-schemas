
-- start  Schema : user_group_summary

CREATE TABLE `user_group_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `group_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `member_count` bigint(20) NOT NULL,
  `visits` bigint(20) NOT NULL,
  `lifetime_purchases` decimal(20,3) NOT NULL,
  `transactions_total` bigint(20) NOT NULL,
  `transactions_today` bigint(20) NOT NULL,
  `total_quantity` bigint(20) NOT NULL,
  `last_transaction_on` timestamp NULL DEFAULT NULL,
  `updated_by` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `primary_total_purchases` decimal(20,3) NOT NULL DEFAULT '0.000',
  `primary_total_visits` bigint(20) NOT NULL DEFAULT '0',
  `primary_purchase_contribution` decimal(20,3) NOT NULL DEFAULT '0.000',
  `primary_total_transactions` bigint(20) NOT NULL DEFAULT '0',
  `primary_group_transactions` bigint(20) NOT NULL DEFAULT '0',
  `primary_total_quantity` bigint(20) NOT NULL DEFAULT '0',
  `primary_group_quantity` bigint(20) NOT NULL DEFAULT '0',
  `primary_group_visits` bigint(20) NOT NULL DEFAULT '0',
  `secondary_group_visits` bigint(20) NOT NULL DEFAULT '0',
  `primary_transactions_today` bigint(20) NOT NULL DEFAULT '0',
  `primary_group_transactions_today` bigint(20) NOT NULL DEFAULT '0',
  `primary_last_transaction_on` timestamp NULL DEFAULT NULL,
  `secondary_last_transaction_on` timestamp NULL DEFAULT NULL,
  `last_regular_transaction_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `UniqueGroup` (`group_id`),
  KEY `auto_time_idx` (`auto_update_time`),
  KEY `idx_group` (`org_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : user_group_summary
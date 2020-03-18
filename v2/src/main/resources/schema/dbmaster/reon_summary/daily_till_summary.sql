
-- start  Schema : daily_till_summary

CREATE TABLE `daily_till_summary` (
  `org_id` int(11) NOT NULL,
  `event_date` date NOT NULL,
  `dim_event_zone_till_id` bigint(20) NOT NULL,
  `dim_event_date_id` bigint(20) NOT NULL,
  `loyalty_registrations` bigint(20) NOT NULL DEFAULT '0',
  `non_loyalty_registrations` bigint(20) NOT NULL DEFAULT '0',
  `member_returns` bigint(20) NOT NULL DEFAULT '0',
  `non_member_returns` bigint(20) NOT NULL DEFAULT '0',
  `number_of_loyalty_bills` bigint(20) NOT NULL DEFAULT '0',
  `number_of_non_loyalty_bills` bigint(20) NOT NULL DEFAULT '0',
  `not_interested_bill_count` bigint(20) NOT NULL DEFAULT '0',
  `new_non_loyal_transacted_customers` bigint(20) NOT NULL DEFAULT '0',
  `new_loyal_transacted_customers` bigint(20) NOT NULL DEFAULT '0',
  `number_of_loyal_repeat_customers` bigint(20) NOT NULL DEFAULT '0',
  `number_of_non_loyal_repeat_customers` bigint(20) NOT NULL DEFAULT '0',
  `points_issued` bigint(20) NOT NULL DEFAULT '0',
  `points_redeemed` bigint(20) NOT NULL DEFAULT '0',
  `coupons_redeemed` bigint(20) NOT NULL DEFAULT '0',
  KEY `org_id` (`org_id`,`dim_event_zone_till_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : daily_till_summary
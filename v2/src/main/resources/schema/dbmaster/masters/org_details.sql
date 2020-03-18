
-- start  Schema : org_details

CREATE TABLE `org_details` (
  `org_id` int(11) NOT NULL,
  `org_logo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_website` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unsubscribe_link` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `social_platforms` longtext COLLATE utf8mb4_unicode_ci,
  `daily_avg_store_transaction` int(11) DEFAULT NULL,
  `avg_transaction_amt` int(11) DEFAULT NULL,
  `avg_items_per_transaction` int(11) DEFAULT NULL,
  `domain_id` int(11) NOT NULL DEFAULT '-1',
  `country_id` int(11) NOT NULL DEFAULT '-1',
  `state_id` int(11) NOT NULL DEFAULT '-1',
  `city_id` int(11) NOT NULL DEFAULT '-1',
  `locality` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pincode` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `finance_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_category` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_details
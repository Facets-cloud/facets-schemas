
-- start  Schema : dvs_stats_voucher_issued

CREATE TABLE `dvs_stats_voucher_issued` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `voucher_series_id` int(11) NOT NULL,
  `voucher_id` int(11) DEFAULT NULL,
  `added_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`voucher_series_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : dvs_stats_voucher_issued

-- start  Schema : emf_dvs_mismatch_summary

CREATE TABLE `emf_dvs_mismatch_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `verification_log_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `transaction_id` int(11) NOT NULL,
  `dvs_no_of_vouchers` int(11) NOT NULL DEFAULT '0',
  `emf_no_of_vouchers` int(11) NOT NULL DEFAULT '0',
  `dvs_voucher_series_id_csv` mediumtext COLLATE utf8mb4_unicode_ci,
  `emf_voucher_series_id_csv` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`customer_id`,`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : emf_dvs_mismatch_summary
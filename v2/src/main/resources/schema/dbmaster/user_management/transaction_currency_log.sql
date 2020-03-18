
-- start  Schema : transaction_currency_log

CREATE TABLE `transaction_currency_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `ref_id` bigint(20) NOT NULL,
  `base_currency_id` int(10) NOT NULL,
  `transaction_currency_id` int(10) NOT NULL,
  `ref_type` enum('REGULAR','RETURN','NOT_INTERESTED','NOT_INTERESTED_RETURN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `local_value` float(10,3) NOT NULL COMMENT 'Local currency value',
  `local_discount` float(10,3) NOT NULL,
  `local_gross_amount` float(10,3) NOT NULL,
  `value` double NOT NULL,
  `ratio` double NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `added_by` bigint(20) NOT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_ref_id_ref_type` (`org_id`,`ref_id`,`ref_type`),
  KEY `auto_update_timestamp` (`auto_update_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : transaction_currency_log
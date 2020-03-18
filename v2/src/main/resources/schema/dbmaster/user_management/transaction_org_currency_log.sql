
-- start  Schema : transaction_org_currency_log

CREATE TABLE `transaction_org_currency_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `ref_id` bigint(20) NOT NULL,
  `base_currency_ratio_id` int(10) NOT NULL,
  `transaction_currency_ratio_id` int(10) NOT NULL,
  `ref_type` enum('REGULAR','RETURN','NOT_INTERESTED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_ref_id_ref_type` (`org_id`,`ref_id`,`ref_type`),
  KEY `auto_update_timestamp` (`auto_update_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : transaction_org_currency_log
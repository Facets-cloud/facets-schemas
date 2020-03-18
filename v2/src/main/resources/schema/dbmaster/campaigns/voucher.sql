
-- start  Schema : voucher

CREATE TABLE `voucher` (
  `voucher_id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `voucher_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pin_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `issued_to` bigint(20) NOT NULL,
  `current_user` bigint(20) NOT NULL,
  `voucher_series_id` int(11) NOT NULL,
  `created_by` bigint(20) NOT NULL COMMENT 'store that created the voucher',
  `test` tinyint(1) NOT NULL DEFAULT '0',
  `amount` float DEFAULT NULL COMMENT 'amount associated with the voucher',
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `loyalty_log_ref_id` bigint(20) DEFAULT NULL,
  `max_allowed_redemptions` int(11) DEFAULT NULL COMMENT 'Maximum number of times this voucher can be redeemed',
  `group_id` int(11) DEFAULT NULL,
  `issued_at_counter_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT 'counter id at which the voucher was issued. Used for calls through store server',
  `rule_map` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`voucher_id`,`org_id`),
  UNIQUE KEY `org_id_2` (`org_id`,`voucher_code`),
  KEY `org_id_3` (`org_id`,`created_date`),
  KEY `user_id` (`org_id`,`issued_to`),
  KEY `voucher_series` (`org_id`,`voucher_series_id`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE,
  KEY `org_voucher_issue` (`org_id`,`voucher_series_id`,`issued_to`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : voucher

-- start  Schema : redemption_request_log

CREATE TABLE `redemption_request_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `till_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `client_ip` int(11) DEFAULT '0',
  `request_scope` enum('POINTS','COUPONS') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_type` enum('REDEEM','ISREDEEMABLE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `client_signature` longtext COLLATE utf8mb4_unicode_ci,
  `redeemed_item` varbinary(100) DEFAULT NULL,
  `skip_validation` tinyint(1) DEFAULT '0',
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `issue_otp` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id_till_id_user_id_scope_idx` (`org_id`,`till_id`,`user_id`),
  KEY `org_id_user_id_idx` (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : redemption_request_log
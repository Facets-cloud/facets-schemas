
-- start  Schema : transaction_type_update_log

CREATE TABLE `transaction_type_update_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `till_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `client_ip` int(11) DEFAULT '0',
  `change_type` enum('RETRO') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `old_id` bigint(20) DEFAULT NULL,
  `new_id` bigint(20) DEFAULT NULL,
  `request_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `client_signature` longtext COLLATE utf8mb4_unicode_ci,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id_type_till_id_idx` (`org_id`,`change_type`,`till_id`),
  KEY `org_id_type_user_id_new_id_idx` (`org_id`,`change_type`,`user_id`,`new_id`),
  KEY `org_id_type_old_id_idx` (`org_id`,`change_type`,`old_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : transaction_type_update_log
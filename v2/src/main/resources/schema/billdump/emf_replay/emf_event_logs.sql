
-- start  Schema : emf_event_logs

CREATE TABLE `emf_event_logs` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `event_name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `till_id` int(11) DEFAULT '0',
  `user_id` bigint(11) NOT NULL,
  `transaction_id` bigint(11) NOT NULL,
  `params` mediumtext COLLATE utf8mb4_unicode_ci,
  `unique_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('IN_PROGRESS','FAIL','SUCCESS','PERMANENT_FAIL','MAX_OUT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `replay_count` tinyint(3) DEFAULT '0',
  `event_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_update_by` bigint(20) DEFAULT NULL,
  `last_update_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `event_id` (`org_id`,`unique_id`),
  KEY `user_txn_idx` (`org_id`,`user_id`,`transaction_id`),
  KEY `status_time_idx` (`status`,`event_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2605167331 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : emf_event_logs
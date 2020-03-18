
-- start  Schema : retro_requests

CREATE TABLE `retro_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `base_type` enum('RETRO') COLLATE utf8mb4_unicode_ci DEFAULT 'RETRO',
  `user_id` bigint(20) DEFAULT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `loyalty_log_id` bigint(20) DEFAULT NULL COMMENT 'loyalty_log_id after marking as retro',
  `reason` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Stores the latest reason (from retro_status_changelog)',
  `comments` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Stores the latest comments (from retro_status_changelog)',
  `auto_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_ref_idx` (`org_id`,`ref_id`),
  KEY `txn_idx` (`org_id`,`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : retro_requests
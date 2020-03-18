
-- start  Schema : fraud_users

CREATE TABLE `fraud_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `status` enum('MARKED','CONFIRMED','RECONFIRMED','NOT_FRAUD','INTERNAL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `reason` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `entered_by` bigint(20) NOT NULL,
  `modified` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`user_id`),
  KEY `org_id_2` (`org_id`,`status`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : fraud_users
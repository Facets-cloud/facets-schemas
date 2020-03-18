
-- start  Schema : merge_customers_log

CREATE TABLE `merge_customers_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `from_user_id` bigint(20) NOT NULL,
  `to_user_id` int(11) NOT NULL,
  `from_user_mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_user_external_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reason` mediumtext COLLATE utf8mb4_unicode_ci,
  `merged_by` bigint(20) NOT NULL,
  `merged_on` datetime NOT NULL,
  `details` mediumblob NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`from_user_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : merge_customers_log
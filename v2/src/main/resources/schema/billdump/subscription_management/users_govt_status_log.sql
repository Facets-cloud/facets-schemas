
-- start  Schema : users_govt_status_log

CREATE TABLE `users_govt_status_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `target_value` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_type` enum('SMS','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SMS',
  `priority` enum('TRANS','BULK') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'BULK',
  `scope_id` int(11) NOT NULL DEFAULT '1',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `added_by_id` int(11) NOT NULL,
  `source_id` int(11) NOT NULL,
  `source_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : users_govt_status_log
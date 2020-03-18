
-- start  Schema : expired_loyalty_info_log

CREATE TABLE `expired_loyalty_info_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `loyalty_current_points` float NOT NULL,
  `points_expired` int(11) NOT NULL DEFAULT '0',
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `entered_by` bigint(20) NOT NULL,
  `expiry_checked_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`user_id`),
  KEY `org_id_2` (`org_id`,`expiry_checked_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : expired_loyalty_info_log

-- start  Schema : awarded_points_log

CREATE TABLE `awarded_points_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `donated_by` int(11) NOT NULL,
  `family_id` int(11) NOT NULL,
  `loyalty_id` bigint(20) NOT NULL,
  `awarded_points` int(11) NOT NULL,
  `redeemed_points` int(11) NOT NULL DEFAULT '0',
  `expired_points` int(11) NOT NULL DEFAULT '0',
  `ref_bill_number` varchar(55) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `awarded_by` bigint(20) NOT NULL,
  `awarded_time` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`user_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : awarded_points_log
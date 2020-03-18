
-- start  Schema : slab_upgrade_log

CREATE TABLE `slab_upgrade_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `loyalty_id` bigint(20) NOT NULL,
  `from_slab_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `to_slab_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `upgrade_bonus_points` int(11) NOT NULL,
  `ref_bill_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `upgraded_by` bigint(20) NOT NULL,
  `upgrade_time` datetime NOT NULL,
  `notes` varchar(55) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `loyalty_id` (`loyalty_id`),
  KEY `org_id` (`org_id`,`user_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : slab_upgrade_log
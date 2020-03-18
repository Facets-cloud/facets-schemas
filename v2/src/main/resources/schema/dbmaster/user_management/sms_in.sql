
-- start  Schema : sms_in

CREATE TABLE `sms_in` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `msg` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `host` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reply` varchar(400) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_no` bigint(20) NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `is_used` tinyint(1) NOT NULL DEFAULT '0',
  `triggered_mappings` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `from_time_isused_idx` (`from`,`time`,`is_used`),
  KEY `org_time_from_idx` (`org_id`,`time`,`from`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : sms_in
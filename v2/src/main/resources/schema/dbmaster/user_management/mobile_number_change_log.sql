
-- start  Schema : mobile_number_change_log

CREATE TABLE `mobile_number_change_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `old_mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `new_mobile` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reported_by` bigint(20) NOT NULL,
  `reporting_time` datetime NOT NULL,
  `validated_by` bigint(20) NOT NULL,
  `validation_time` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `user_id` (`user_id`,`old_mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mobile_number_change_log
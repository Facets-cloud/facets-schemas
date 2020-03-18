
-- start  Schema : sms_params_logs

CREATE TABLE `sms_params_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sms_params_id` int(11) NOT NULL,
  `body` mediumtext COLLATE utf8mb4_unicode_ci,
  `sms_unsub_type` enum('MISSED_CALL','SMS') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sms_step_number` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `optout_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unsub_text` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=769 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : sms_params_logs
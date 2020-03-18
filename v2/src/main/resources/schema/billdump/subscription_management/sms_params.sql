
-- start  Schema : sms_params

CREATE TABLE `sms_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `body` mediumtext COLLATE utf8mb4_unicode_ci,
  `sms_unsub_type` enum('MISSED_CALL','SMS') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sms_step_number` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `optout_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unsub_text` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_code_idx` (`optout_number`,`unsub_text`)
) ENGINE=InnoDB AUTO_INCREMENT=374 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : sms_params

-- start  Schema : email_params_logs

CREATE TABLE `email_params_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_params_id` int(11) NOT NULL,
  `subject` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `body` mediumtext COLLATE utf8mb4_unicode_ci,
  `email_step_number` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email_unsub_type` enum('SIMPLE','PREFERENTIAL') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sender_label` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sender_email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_service_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=409 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_params_logs
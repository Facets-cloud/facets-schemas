
-- start  Schema : survey_message_details

CREATE TABLE `survey_message_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `nsadmin_id` bigint(20) NOT NULL,
  `sent_messages_log_id` bigint(20) NOT NULL,
  `dvs_campaign_id` bigint(20) DEFAULT NULL,
  `survey_campaign_id` bigint(20) DEFAULT NULL,
  `survey_id` int(11) DEFAULT NULL,
  `form_id` int(11) DEFAULT NULL,
  `type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token_id` int(11) NOT NULL DEFAULT '-1' COMMENT 'id of the token in the survey_tokens table',
  `token` mediumtext COLLATE utf8mb4_unicode_ci,
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_update_time` (`org_id`,`auto_update_time`),
  KEY `survey_index` (`org_id`,`customer_id`,`sent_messages_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : survey_message_details
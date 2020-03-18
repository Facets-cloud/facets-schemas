
-- start  Schema : survey_tokens

CREATE TABLE `survey_tokens` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `survey_id` int(11) unsigned NOT NULL,
  `survey_form_id` int(11) unsigned NOT NULL,
  `token_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_used` tinyint(1) NOT NULL DEFAULT '0',
  `created_on` date NOT NULL,
  `issued_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ADMIN_USER',
  `bill_id` int(11) NOT NULL DEFAULT '0',
  `issued_by` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `communication_id` int(11) NOT NULL,
  `message_source_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT 'EMF',
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : survey_tokens

-- start  Schema : survey_fields

CREATE TABLE `survey_fields` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) unsigned NOT NULL,
  `form_id` int(11) unsigned NOT NULL,
  `survey_id` int(11) unsigned NOT NULL,
  `parent_id` int(11) NOT NULL DEFAULT '-1',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `is_nps_question` tinyint(1) NOT NULL,
  `field_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `field_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_options` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_label` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `default_value` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_placeholder` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `params` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_token_field` tinyint(1) NOT NULL,
  `field_help_text` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_compulsory` tinyint(1) NOT NULL,
  `order_number` int(11) unsigned NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tag` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `form_id` (`form_id`,`field_name`),
  KEY `org_id` (`org_id`,`survey_id`,`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : survey_fields
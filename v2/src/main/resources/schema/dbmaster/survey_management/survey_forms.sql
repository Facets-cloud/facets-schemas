
-- start  Schema : survey_forms

CREATE TABLE `survey_forms` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) unsigned NOT NULL,
  `survey_id` int(11) unsigned NOT NULL,
  `survey_form_code` int(10) NOT NULL,
  `form_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `form_title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `form_theme_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_default_form` tinyint(1) NOT NULL,
  `is_published` tinyint(1) NOT NULL DEFAULT '0',
  `fs_link` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `number_of_responses` int(11) NOT NULL,
  `number_of_promotors` int(11) NOT NULL,
  `number_of_detractors` int(11) NOT NULL,
  `confirmation_message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `brand_logo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `brand_website` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `survey_id` (`survey_id`,`survey_form_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : survey_forms
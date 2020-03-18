
-- start  Schema : survey_details

CREATE TABLE `survey_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) unsigned NOT NULL,
  `campaign_id` int(11) unsigned NOT NULL,
  `number_of_forms` int(11) unsigned NOT NULL,
  `number_of_responses` int(11) unsigned NOT NULL,
  `number_of_promotors` int(11) unsigned NOT NULL,
  `number_of_detractors` int(11) unsigned NOT NULL,
  `survey_key` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `survey_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ONLINE',
  `brand_logo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `brand_website` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'json format',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `external_ref_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : survey_details
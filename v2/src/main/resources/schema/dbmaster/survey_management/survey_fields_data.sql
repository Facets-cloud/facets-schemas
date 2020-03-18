
-- start  Schema : survey_fields_data

CREATE TABLE `survey_fields_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) unsigned NOT NULL,
  `survey_id` int(11) unsigned NOT NULL,
  `form_id` int(11) unsigned NOT NULL,
  `survey_field_id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `token_id` int(11) NOT NULL,
  `field_value` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `entityType` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `entityId` int(11) DEFAULT NULL,
  `responseChannel` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : survey_fields_data
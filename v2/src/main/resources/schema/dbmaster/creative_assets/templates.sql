
-- start  Schema : templates

CREATE TABLE `templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `template_type_id` int(11) NOT NULL,
  `scope` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ORG',
  `tag` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'GENERAL',
  `file_service_params` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_default` tinyint(1) NOT NULL DEFAULT '0',
  `is_preview_generated` tinyint(1) NOT NULL DEFAULT '0',
  `is_email_client_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_favourite` tinyint(1) NOT NULL DEFAULT '0',
  `parent_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`),
  KEY `template_name_3` (`template_name`,`template_type_id`),
  KEY `temp1` (`template_type_id`),
  KEY `scope` (`scope`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : templates
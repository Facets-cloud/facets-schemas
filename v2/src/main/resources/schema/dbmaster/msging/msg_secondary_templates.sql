
-- start  Schema : msg_secondary_templates

CREATE TABLE `msg_secondary_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `module` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_id` int(11) NOT NULL,
  `msg_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `msg_body` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `lang_id` int(11) NOT NULL,
  `default_params` longtext COLLATE utf8mb4_unicode_ci,
  `is_deleted` tinyint(1) DEFAULT '0',
  `last_updated_by` int(11) DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_ref_id` (`org_id`,`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : msg_secondary_templates
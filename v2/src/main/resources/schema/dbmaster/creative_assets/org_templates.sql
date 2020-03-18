
-- start  Schema : org_templates

CREATE TABLE `org_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `ref_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lang_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`template_id`,`ref_id`),
  KEY `org_id_2` (`org_id`,`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_templates
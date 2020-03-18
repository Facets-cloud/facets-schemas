
-- start  Schema : custom_field_templates

CREATE TABLE `custom_field_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('select','text','datepicker') COLLATE utf8mb4_unicode_ci NOT NULL,
  `datatype` enum('Boolean','String','Integer','Double') COLLATE utf8mb4_unicode_ci NOT NULL,
  `label` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `scope` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `default` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` bigint(20) NOT NULL DEFAULT '-1',
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : custom_field_templates
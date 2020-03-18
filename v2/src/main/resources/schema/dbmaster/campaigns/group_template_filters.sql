
-- start  Schema : group_template_filters

CREATE TABLE `group_template_filters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_template_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `filter_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `filter_explaination` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `custom_ids` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `group_template_id` (`group_template_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : group_template_filters
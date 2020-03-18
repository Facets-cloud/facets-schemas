
-- start  Schema : selection_filter

CREATE TABLE `selection_filter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `audience_group_id` int(11) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `params` longtext COLLATE utf8mb4_unicode_ci,
  `filter_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `filter_explaination` longtext COLLATE utf8mb4_unicode_ci,
  `no_of_customers` int(11) NOT NULL,
  `custom_ids` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  KEY `audience_group_id` (`audience_group_id`,`filter_type`(1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : selection_filter
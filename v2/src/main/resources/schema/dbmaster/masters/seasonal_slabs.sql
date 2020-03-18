
-- start  Schema : seasonal_slabs

CREATE TABLE `seasonal_slabs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `period_from` date NOT NULL,
  `period_to` date NOT NULL,
  `for_stores_json` mediumtext COLLATE utf8mb4_unicode_ci,
  `in_zones` mediumtext COLLATE utf8mb4_unicode_ci,
  `params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` bigint(20) NOT NULL,
  `last_modified` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : seasonal_slabs
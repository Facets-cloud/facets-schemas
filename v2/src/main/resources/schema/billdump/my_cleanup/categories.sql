
-- start  Schema : categories

CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `parent_id` int(11) NOT NULL,
  `added_by` bigint(20) NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `is_valid` tinyint(1) DEFAULT '1',
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_name` (`org_id`,`code`),
  KEY `org_parent_id` (`org_id`,`parent_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=8204386 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='classification of products are mentioned in the table';


-- end  Schema : categories
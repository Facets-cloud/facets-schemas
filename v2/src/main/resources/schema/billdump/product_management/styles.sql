
-- start  Schema : styles

CREATE TABLE `styles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Will be copied from sizes table, but can be overwritten for an org',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `added_by` bigint(20) NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `is_valid` tinyint(1) DEFAULT '1',
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_code` (`org_id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=184465 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='styles';


-- end  Schema : styles
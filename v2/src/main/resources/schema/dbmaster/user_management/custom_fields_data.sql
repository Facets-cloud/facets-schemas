
-- start  Schema : custom_fields_data

CREATE TABLE `custom_fields_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `cf_id` int(11) NOT NULL COMMENT 'reference to the custom field',
  `assoc_id` bigint(20) NOT NULL,
  `value` longtext COLLATE utf8mb4_unicode_ci,
  `entered_by` bigint(20) NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`assoc_id`,`cf_id`),
  KEY `org_id_2` (`org_id`,`cf_id`),
  KEY `org_time_idx` (`org_id`,`modified`) USING BTREE,
  KEY `auto_time_idx` (`modified`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : custom_fields_data
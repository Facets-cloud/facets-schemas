
-- start  Schema : custom_fields_data_log

CREATE TABLE `custom_fields_data_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `update_id` bigint(20) NOT NULL,
  `org_id` int(11) NOT NULL,
  `cf_id` int(11) NOT NULL COMMENT 'reference to the custom field',
  `assoc_id` bigint(20) NOT NULL,
  `value` mediumtext COLLATE utf8mb4_unicode_ci,
  `entered_by` bigint(20) NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `update_id` (`update_id`,`cf_id`),
  KEY `org_id_2` (`org_id`,`cf_id`),
  KEY `org_id` (`org_id`,`assoc_id`,`cf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : custom_fields_data_log
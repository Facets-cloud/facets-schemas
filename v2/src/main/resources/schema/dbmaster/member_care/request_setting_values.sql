
-- start  Schema : request_setting_values

CREATE TABLE `request_setting_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_id` int(11) DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `value` mediumtext COLLATE utf8mb4_unicode_ci,
  `created_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created_by` int(11) DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_key_id` (`org_id`,`key_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : request_setting_values
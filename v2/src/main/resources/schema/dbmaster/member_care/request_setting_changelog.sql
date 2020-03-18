
-- start  Schema : request_setting_changelog

CREATE TABLE `request_setting_changelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_id` int(11) DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `old_value` mediumtext COLLATE utf8mb4_unicode_ci,
  `new_value` mediumtext COLLATE utf8mb4_unicode_ci,
  `updated_by` int(11) DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : request_setting_changelog
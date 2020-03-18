
-- start  Schema : org_data

CREATE TABLE `org_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_tag` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'text',
  `value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `persistent` int(11) NOT NULL DEFAULT '0',
  `added` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_data
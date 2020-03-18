
-- start  Schema : services

CREATE TABLE `services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_code` mediumtext COLLATE utf8mb4_unicode_ci,
  `service_type_id` int(11) NOT NULL,
  `rule` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_hidden` tinyint(1) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : services

-- start  Schema : meta_sizes

CREATE TABLE `meta_sizes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `size_family` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Size family saying US, EU, etc',
  `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'used to measure what object',
  `parent_id` int(11) NOT NULL DEFAULT '0',
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_type` (`code`,`type`,`size_family`)
) ENGINE=InnoDB AUTO_INCREMENT=1440 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Colors meta table';


-- end  Schema : meta_sizes
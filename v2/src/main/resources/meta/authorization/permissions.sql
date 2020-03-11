
-- start  Schema : permissions

CREATE TABLE `permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assoc_module` int(11) NOT NULL DEFAULT '-1' COMMENT 'module associated with this permission, give -1 if it is meant for more modules',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : permissions
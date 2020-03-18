
-- start  Schema : permission_hierarchy

CREATE TABLE `permission_hierarchy` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'reference',
  `parent_permission_id` int(11) NOT NULL,
  `child_permission_id` int(11) NOT NULL,
  `notes` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `child_permission_id` (`child_permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : permission_hierarchy
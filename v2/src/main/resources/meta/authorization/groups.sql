
-- start  Schema : groups

CREATE TABLE `groups` (
  `org_id` bigint(20) NOT NULL,
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `org_id` (`org_id`,`group_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3790 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : groups
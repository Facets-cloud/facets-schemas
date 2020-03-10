
-- start  Schema : user_activity_log

CREATE TABLE `user_activity_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `user_ip` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `activity_type` enum('VIEW','CREATE','UPDATE','DELETE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'VIEW',
  `action_type` enum('SINGLE','BULK') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SINGLE',
  `reference_org_id` int(11) DEFAULT NULL,
  `user_ids` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `activity_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ref_id` (`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Stores all the activity of a user.';


-- end  Schema : user_activity_log
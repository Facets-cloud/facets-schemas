
-- start  Schema : app_messages

CREATE TABLE `app_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('TEXT','IMAGE','HTML') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TEXT',
  `body` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('UNREAD','SENT','READ','DELETED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'UNREAD',
  `priority` enum('HIGH','LOW') COLLATE utf8mb4_unicode_ci NOT NULL,
  `received_time` datetime NOT NULL,
  `sent_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : app_messages
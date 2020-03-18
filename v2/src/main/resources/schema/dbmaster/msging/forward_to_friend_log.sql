
-- start  Schema : forward_to_friend_log

CREATE TABLE `forward_to_friend_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `outbox_id` int(11) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `campaign_id` int(11) NOT NULL DEFAULT '-1',
  `nsadmin_id` int(11) NOT NULL,
  `message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `receiver` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `receiver_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `sent_time` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `index2` (`user_id`,`outbox_id`,`org_id`,`nsadmin_id`),
  KEY `index3` (`user_id`,`org_id`),
  KEY `org_id` (`org_id`),
  KEY `index4` (`org_id`,`campaign_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : forward_to_friend_log
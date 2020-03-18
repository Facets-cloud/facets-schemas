
-- start  Schema : email_bounce_stats

CREATE TABLE `email_bounce_stats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `messageId` int(11) NOT NULL,
  `campaign_id` bigint(20) NOT NULL DEFAULT '-1',
  `group_id` bigint(20) NOT NULL DEFAULT '-1',
  `org_id` bigint(20) NOT NULL DEFAULT '-1',
  `user_id` bigint(20) NOT NULL,
  `last_read_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `messageId_2` (`messageId`,`campaign_id`,`group_id`,`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_bounce_stats
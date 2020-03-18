
-- start  Schema : user_email_stats_archive

CREATE TABLE `user_email_stats_archive` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campaign_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `msg_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `browser` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `platform` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile_device` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_agent` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `opened_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_email_stats_archive

-- start  Schema : user_email_stats

CREATE TABLE `user_email_stats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `campaign_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `msg_id` int(11) NOT NULL,
  `clicks` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `browser_id` int(11) DEFAULT NULL,
  `platform_id` int(11) DEFAULT NULL,
  `mobile_device_id` int(11) DEFAULT NULL,
  `user_agent` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `opened_on` datetime NOT NULL,
  `type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CAMPAIGN',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `msg_id` (`msg_id`,`user_id`),
  KEY `msg_id_2` (`org_id`,`campaign_id`,`group_id`,`msg_id`),
  KEY `opened_on` (`opened_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_email_stats
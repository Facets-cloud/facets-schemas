
-- start  Schema : email_stats

CREATE TABLE `email_stats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `campaign_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `msg_id` int(11) NOT NULL,
  `read_count` int(11) NOT NULL DEFAULT '0',
  `last_updated_on` datetime NOT NULL,
  `type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CAMPAIGN',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `group_id` (`group_id`,`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_stats
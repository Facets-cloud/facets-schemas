
-- start  Schema : email_open_stats

CREATE TABLE `email_open_stats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `messageId` int(11) NOT NULL,
  `opened_by` int(11) NOT NULL,
  `campaign_id` bigint(20) NOT NULL DEFAULT '-1',
  `group_id` bigint(20) NOT NULL DEFAULT '-1',
  `org_id` bigint(20) NOT NULL DEFAULT '-1',
  `last_read_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CAMPAIGN',
  PRIMARY KEY (`id`,`org_id`),
  KEY `messageId` (`messageId`),
  KEY `last_read_at` (`last_read_at`),
  KEY `org_id_opened_by` (`org_id`,`opened_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_open_stats

-- start  Schema : bulksms_campaign

CREATE TABLE `bulksms_campaign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `campaign_id` int(11) NOT NULL,
  `msg_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `sent_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `org_id` bigint(20) NOT NULL,
  `queue_id` bigint(20) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `campaign_id` (`campaign_id`,`msg_id`),
  KEY `msg_id` (`msg_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bulksms_campaign
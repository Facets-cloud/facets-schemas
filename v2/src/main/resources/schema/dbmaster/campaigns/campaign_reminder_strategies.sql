
-- start  Schema : campaign_reminder_strategies

CREATE TABLE `campaign_reminder_strategies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contacted_filter_id` int(11) NOT NULL,
  `responder_filter_id` int(11) NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : campaign_reminder_strategies
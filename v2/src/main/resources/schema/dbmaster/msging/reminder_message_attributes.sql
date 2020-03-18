
-- start  Schema : reminder_message_attributes

CREATE TABLE `reminder_message_attributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `reminder_strategy_id` int(11) NOT NULL,
  `contacted_audience_group_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `responder_audience_group_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`campaign_id`,`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : reminder_message_attributes
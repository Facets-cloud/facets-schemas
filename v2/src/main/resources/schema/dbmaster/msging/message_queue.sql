
-- start  Schema : message_queue

CREATE TABLE `message_queue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` enum('EMAIL','SMS','SMS_REMINDER','EMAIL_REMINDER','CALL_TASK','CALL_TASK_REMINDER','CUSTOMER_TASK','SMS_EXPIRY_REMINDER','EMAIL_EXPIRY_REMINDER','WECHAT','WECHAT_REMINDER','MOBILEPUSH','MOBILEPUSH_REMINDER','FACEBOOK','GOOGLE','TWITTER','SMS_CAMPAIGN_REMINDER','EMAIL_CAMPAIGN_REMINDER','WECHAT_CAMPAIGN_REMINDER','MOBILEPUSH_CAMPAIGN_REMINDER','CALLTASK_CAMPAIGN_REMINDER','FACEBOOK_CAMPAIGN_REMINDER','GOOGLE_CAMPAIGN_REMINDER','TWITTER_CAMPAIGN_REMINDER','LINE','LINE_REMINDER','LINE_CAMPAIGN_REMINDER') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `scheduled_on` datetime NOT NULL,
  `scheduled_by` int(11) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `approved_by` int(11) NOT NULL,
  `default_arguments` longtext COLLATE utf8mb4_unicode_ci,
  `status` enum('OPEN','SENT','REJECTED','MIGRATED') COLLATE utf8mb4_unicode_ci DEFAULT 'OPEN',
  `scheduled_type` enum('IMMEDIATELY','SCHEDULED','PARTICULAR_DATE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `params` longtext COLLATE utf8mb4_unicode_ci,
  `approved` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id_campaign_id` (`org_id`,`campaign_id`),
  KEY `grp_campaign` (`campaign_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : message_queue
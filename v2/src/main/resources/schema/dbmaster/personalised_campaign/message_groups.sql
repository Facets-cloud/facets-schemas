
-- start  Schema : message_groups

CREATE TABLE `message_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('EMAIL','SMS','SMS_REMINDER','EMAIL_REMINDER','CALL_TASK','CALL_TASK_REMINDER','CUSTOMER_TASK','SMS_EXPIRY_REMINDER','EMAIL_EXPIRY_REMINDER','WECHAT','WECHAT_REMINDER','MOBILEPUSH','MOBILEPUSH_REMINDER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SMS',
  `channel_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `parent_group_id` int(11) NOT NULL,
  `scheduled_on` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `scheduled_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `approved_by` int(11) NOT NULL,
  `default_arguments` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('OPEN','SENT','REJECTED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPEN',
  `scheduled_type` enum('IMMEDIATELY','SCHEDULED','PARTICULAR_DATE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `req_id` int(11) NOT NULL,
  `approved` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : message_groups
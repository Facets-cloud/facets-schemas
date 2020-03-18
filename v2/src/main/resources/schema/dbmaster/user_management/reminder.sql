
-- start  Schema : reminder

CREATE TABLE `reminder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `refrence_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `audience_group_ids` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pending_groups` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` enum('RUNNING','PAUSE','STOP','REMINDING','OPEN','REMINDED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reminder_type` enum('CAMPAIGN','REPORTS','REFERRAL','FEEDBACK','FTP_IMPORT','OPTIN_REMINDER','HEALTH','EMF','POINTS_ENGINE','APILOGUPDATE','FTP_DUMP','ARCHIVAL','EXPORT_SCHEDULE_TASK','CREATIVE_ASSETS','EBILL_SERVICE','EMAIL_STATUS_TASK','SCA_NOTIFICATION','TIMELINE','VOUCHER_REMINDER','PERSONALISED_CAMPAIGN') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `frequency` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `limits` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scheduled_by` int(11) NOT NULL,
  `scheduler_task_id` int(11) NOT NULL,
  `created_date` date NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : reminder
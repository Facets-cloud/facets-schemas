
-- start  Schema : expiry_reminders_sent_stats

CREATE TABLE `expiry_reminders_sent_stats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL,
  `reminders_sent` int(11) NOT NULL,
  `sms_template` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SMS template used for sending reminders.',
  `sent_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : expiry_reminders_sent_stats

-- start  Schema : expiry_reminder_skip_info

CREATE TABLE `expiry_reminder_skip_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL,
  `skip_reminder_date` date NOT NULL COMMENT 'Date on which reminders should skip.',
  `created_on` datetime NOT NULL COMMENT 'When this skip is added',
  `created_by` int(11) NOT NULL COMMENT 'User id',
  `reason` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Reason for creating this record.',
  `is_skipped` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Is this record processed...',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`program_id`,`skip_reminder_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : expiry_reminder_skip_info
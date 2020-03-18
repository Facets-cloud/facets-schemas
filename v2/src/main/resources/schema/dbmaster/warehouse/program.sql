
-- start  Schema : program

CREATE TABLE `program` (
  `id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'External name for the program',
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'External description for the program',
  `is_active` tinyint(1) NOT NULL COMMENT 'Checks if the program is active or not',
  `is_default` tinyint(1) NOT NULL DEFAULT '0',
  `last_activated` datetime NOT NULL COMMENT 'Last date when the program was activated',
  `slab_upgrade_point_category_id` int(11) NOT NULL COMMENT 'the point category on which to base the slab upgrade rules',
  `slab_upgrade_stategy_id` int(11) NOT NULL COMMENT 'the upgrade strategy to be used for pro-rated awarding',
  `slab_upgrade_mode` enum('EAGER','DYNAMIC','LAZY') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Whether to perform the slab upgrade before or during or after the event',
  `slab_upgrade_rule_identifier` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'rule from where the slab upgrade checking has to happen. Will be used for eager / lazy slab upgrade mode',
  `redeemable_point_category_id` int(11) DEFAULT NULL COMMENT 'point category which is redeemable for the program',
  `points_currency_ratio` decimal(20,10) NOT NULL DEFAULT '1.0000000000' COMMENT 'Each point is worth how much of the currency value. Eg: A value of 0.5 means, each point is worth 0.5 rupees',
  `reminder_sms_template` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Reminder SMS template.',
  `reminder_mail_subject` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Reminder email subject.',
  `reminder_mail_body` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Reminder email body.',
  `reminder_before_days_csv` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'CSV values to store number of days before which reminder message will be sent.',
  `reminder_min_expirypoints` int(11) NOT NULL DEFAULT '1' COMMENT 'Minimum expiry points for which reminder message to be sent..',
  `contact_info` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Mailing address for the program. It will be a csv.',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `round_decimals` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_time_idx` (`last_activated`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Information about all the programs';


-- end  Schema : program
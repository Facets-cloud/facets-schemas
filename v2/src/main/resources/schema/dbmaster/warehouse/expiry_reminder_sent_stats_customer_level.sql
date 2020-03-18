
-- start  Schema : expiry_reminder_sent_stats_customer_level

CREATE TABLE `expiry_reminder_sent_stats_customer_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `program_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `message_id` bigint(20) NOT NULL,
  `points_expiring` double NOT NULL,
  `expiry_date` datetime NOT NULL,
  `num_of_days_before` int(11) NOT NULL,
  `type` enum('SMS','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SMS' COMMENT 'Type of reminders',
  `created_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `customer` (`program_id`,`customer_id`),
  KEY `auto_update_time` (`auto_update_time`,`program_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : expiry_reminder_sent_stats_customer_level
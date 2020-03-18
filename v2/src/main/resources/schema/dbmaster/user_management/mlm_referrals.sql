
-- start  Schema : mlm_referrals

CREATE TABLE `mlm_referrals` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `referrer_id` bigint(20) NOT NULL,
  `referee_mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `referee_email` varchar(55) COLLATE utf8mb4_unicode_ci NOT NULL,
  `referral_date` datetime NOT NULL,
  `num_reminders` int(11) NOT NULL DEFAULT '0' COMMENT 'The number of reminders sent to the customer after the first SMS',
  `joined_date` datetime DEFAULT NULL COMMENT 'Date on which this Referee joined. NULL indicates no joining',
  `referred_at_store` bigint(20) NOT NULL,
  `referee_id_joined` bigint(20) DEFAULT NULL COMMENT 'user id of the referee after joining',
  `last_reminded` datetime DEFAULT NULL COMMENT 'when was the last time the user was reminded',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`referee_mobile`,`referrer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mlm_referrals

-- start  Schema : referees

CREATE TABLE `referees` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `referrer_id` bigint(20) NOT NULL,
  `referrer_user_id` int(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `campaign_id` int(20) NOT NULL COMMENT 'The campaign id',
  `user_id` bigint(20) NOT NULL COMMENT 'referee user id',
  `till_id` int(20) NOT NULL,
  `context_id` int(20) NOT NULL,
  `event` enum('TRANSACTION','REGISTRATION') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'which event caused this referee to come into the system',
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_auto_update_time` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : referees
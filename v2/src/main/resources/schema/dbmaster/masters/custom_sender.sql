
-- start  Schema : custom_sender

CREATE TABLE `custom_sender` (
  `org_id` bigint(20) NOT NULL,
  `sender_gsm` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_cdma` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_label` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `replyto_email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_activated` datetime NOT NULL,
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : custom_sender
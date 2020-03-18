
-- start  Schema : entity_custom_sender

CREATE TABLE `entity_custom_sender` (
  `org_id` int(11) NOT NULL,
  `entity_id` int(11) NOT NULL DEFAULT '-1',
  `entity_type` enum('CONCEPT','ORG') COLLATE utf8mb4_unicode_ci DEFAULT 'CONCEPT',
  `sender_gsm` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_cdma` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_label` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `replyto_email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender_email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_by` int(11) NOT NULL COMMENT 'maps to the admin users table',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `entity` (`org_id`,`entity_id`,`entity_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : entity_custom_sender
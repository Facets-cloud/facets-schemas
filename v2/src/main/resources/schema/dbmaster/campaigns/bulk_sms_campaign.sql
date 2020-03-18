
-- start  Schema : bulk_sms_campaign

CREATE TABLE `bulk_sms_campaign` (
  `campaign_id` bigint(20) NOT NULL,
  `message_template` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`campaign_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bulk_sms_campaign
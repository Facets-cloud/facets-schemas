
-- start  Schema : org_sms_credits

CREATE TABLE `org_sms_credits` (
  `org_id` bigint(20) NOT NULL,
  `value_sms_credits` int(11) NOT NULL DEFAULT '0',
  `bulk_sms_credits` int(11) NOT NULL DEFAULT '0',
  `user_credits` int(11) NOT NULL DEFAULT '0',
  `created_by` bigint(20) NOT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_sms_credits
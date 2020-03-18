
-- start  Schema : credits_log

CREATE TABLE `credits_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `value_sms_credits` int(11) DEFAULT NULL,
  `bulk_sms_credits` int(11) DEFAULT NULL,
  `user_credits` int(11) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `added` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : credits_log
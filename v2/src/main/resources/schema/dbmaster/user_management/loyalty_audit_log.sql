
-- start  Schema : loyalty_audit_log

CREATE TABLE `loyalty_audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `event_type` enum('conversion','non_loyalty_registration') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'non_loyalty_registration',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `org_id` bigint(20) NOT NULL,
  `meta_data` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED;


-- end  Schema : loyalty_audit_log
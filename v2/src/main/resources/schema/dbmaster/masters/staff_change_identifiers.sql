
-- start  Schema : staff_change_identifiers

CREATE TABLE `staff_change_identifiers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `old_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `new_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` enum('EMAIL','MOBILE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `refId` int(11) DEFAULT NULL,
  `status` enum('SEND_OTP','SUCCESS','FAILED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `notes` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_type_idx` (`org_id`,`type`),
  KEY `org_req_idx` (`org_id`,`refId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : staff_change_identifiers

-- start  Schema : verification_history

CREATE TABLE `verification_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `type` enum('MOBILE','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `requested_value` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `verification_code` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `requested_time` datetime NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `entered_by` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`type`,`requested_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : verification_history
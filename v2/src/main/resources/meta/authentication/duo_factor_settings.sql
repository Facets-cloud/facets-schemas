
-- start  Schema : duo_factor_settings

CREATE TABLE `duo_factor_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `type` enum('TILL','ADMIN_USER','STR_SERVER','ASSOCIATE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_enabled` tinyint(4) DEFAULT '0',
  `otp_validity_in_hours` int(11) DEFAULT NULL,
  `session_validity` int(11) DEFAULT NULL COMMENT 'validity of session in hours',
  `module` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `otp_session_validity` int(11) DEFAULT '15' COMMENT 'validity of session in minutes',
  `two_factor_validity_hours` int(11) DEFAULT '24',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq` (`org_id`,`type`,`module`)
) ENGINE=InnoDB AUTO_INCREMENT=967 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : duo_factor_settings
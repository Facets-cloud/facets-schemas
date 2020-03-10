
-- start  Schema : verification_body_template

CREATE TABLE `verification_body_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `identifier_type` enum('USERNAME','MOBILE','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USERNAME',
  `activity_type` enum('RESET_PASSWORD','FORGOT_PASSWORD','OTP_LOGIN','VALIDATE_IDENTIFIER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `body` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`),
  KEY `idx1` (`org_id`,`identifier_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : verification_body_template
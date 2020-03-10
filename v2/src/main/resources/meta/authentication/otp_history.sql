
-- start  Schema : otp_history

CREATE TABLE `otp_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `otp` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_type` enum('LOGIN','RESET_PASSWORD','FORGOT_PASSWORD','OTP_LOGIN','VALIDATE_EMAIL','VALIDATE_MOBILE','CHANGE_MOBILE','CHANGE_EMAIL') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ref_id` bigint(20) DEFAULT NULL,
  `loggable_user_id` bigint(20) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_on` timestamp NULL DEFAULT NULL,
  `status` enum('VALIDATED','OPEN','EXPIRED') COLLATE utf8mb4_unicode_ci DEFAULT 'OPEN',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=141805 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : otp_history
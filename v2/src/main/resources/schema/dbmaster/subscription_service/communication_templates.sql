
-- start  Schema : communication_templates

CREATE TABLE `communication_templates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `configuration_id` bigint(20) NOT NULL,
  `type` enum('OPTIN_TEXT','OPTOUT_TEXT','OPTIN_SUCCESS','OPTOUT_SUCCESS','CONFIRMATION_REQUEST','TRANS_OPTIN_TEXT','TRANS_OPTOUT_TEXT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `subject_template` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message_template` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sender` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` bigint(20) NOT NULL,
  `added_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_config_id_type_idx` (`org_id`,`configuration_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : communication_templates
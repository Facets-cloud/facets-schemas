
-- start  Schema : subscription_configuration

CREATE TABLE `subscription_configuration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `org_unit_id` bigint(20) NOT NULL DEFAULT '-1',
  `org_source_id` bigint(20) NOT NULL,
  `comm_channel_id` bigint(20) NOT NULL,
  `optin_strategy` enum('DOUBLE','SINGLE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SINGLE',
  `send_optin_success_message` tinyint(4) NOT NULL DEFAULT '0',
  `default_status` enum('OPTIN','OPTOUT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` bigint(20) NOT NULL,
  `added_on` datetime NOT NULL,
  `updated_by` bigint(11) NOT NULL,
  `updated_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `send_optout_success_message` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `source_comm_strategy_unq` (`org_id`,`org_unit_id`,`org_source_id`,`comm_channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : subscription_configuration
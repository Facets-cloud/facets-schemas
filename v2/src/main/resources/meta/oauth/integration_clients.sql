
-- start  Schema : integration_clients

CREATE TABLE `integration_clients` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `client_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `client_secret` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `default_till_id` bigint(20) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `last_updated_on` timestamp NULL DEFAULT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '0',
  `expiration_token_time` bigint(20) DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `client_key` (`client_key`),
  UNIQUE KEY `org_id` (`org_id`,`client_name`),
  KEY `auto_time_idx` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : integration_clients
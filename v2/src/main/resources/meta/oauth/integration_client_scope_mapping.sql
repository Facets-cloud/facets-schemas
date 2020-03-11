
-- start  Schema : integration_client_scope_mapping

CREATE TABLE `integration_client_scope_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `permission` enum('READ','WRITE','READWRITE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'READ',
  `entity_id` bigint(20) NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `client_id_idx` (`client_id`,`org_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : integration_client_scope_mapping
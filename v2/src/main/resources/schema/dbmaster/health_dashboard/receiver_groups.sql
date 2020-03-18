
-- start  Schema : receiver_groups

CREATE TABLE `receiver_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) DEFAULT NULL,
  `module_id` int(11) NOT NULL,
  `context_id` int(11) NOT NULL,
  `channel` enum('SMS','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `recipient_type` enum('CAP_POC','BRAND_POC') COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `created_by` int(11) NOT NULL DEFAULT '-1',
  `created_time` datetime NOT NULL,
  `updated_by` int(11) NOT NULL DEFAULT '-1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_receiever_group` (`org_id`,`module_id`,`context_id`,`channel`,`recipient_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : receiver_groups
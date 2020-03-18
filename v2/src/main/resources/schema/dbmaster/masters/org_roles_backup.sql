
-- start  Schema : org_roles_backup

CREATE TABLE `org_roles_backup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `role_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_type` enum('ORG','ZONE','CONCEPT','STORE','TILL','STR_SERVER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ORG',
  `parent_role_id` int(11) NOT NULL,
  `created_by` int(11) NOT NULL,
  `approver` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`),
  KEY `created_by` (`created_by`),
  KEY `last_updated_by` (`last_updated_by`),
  KEY `parent_role_id` (`parent_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_roles_backup
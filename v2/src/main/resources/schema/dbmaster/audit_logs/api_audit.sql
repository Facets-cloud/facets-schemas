
-- start  Schema : api_audit

CREATE TABLE `api_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `org_id` int(11) NOT NULL,
  `entity_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `entity_id` int(11) NOT NULL,
  `operation` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `module` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'API' COMMENT 'Module which performed the operation',
  `reference_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Unique identifier in module to identify the operation',
  `old_data` text COLLATE utf8mb4_unicode_ci COMMENT 'Old information',
  `new_data` text COLLATE utf8mb4_unicode_ci COMMENT 'New information',
  `operation_by` int(11) NOT NULL COMMENT 'Org-entity that performed the operation',
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `params` text COLLATE utf8mb4_unicode_ci COMMENT 'Additional information about operation',
  PRIMARY KEY (`id`),
  KEY `lookupEntityWithTime` (`org_id`,`entity_type`,`entity_id`,`auto_update_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='This table logs the changes performed on mutable API entities';


-- end  Schema : api_audit
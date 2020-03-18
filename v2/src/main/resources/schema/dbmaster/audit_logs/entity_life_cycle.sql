
-- start  Schema : entity_life_cycle

CREATE TABLE `entity_life_cycle` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `org_id` int(11) NOT NULL,
  `entity_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'CUSTOMER OR PROFILE',
  `entity_id` int(11) NOT NULL,
  `org_source_id` int(11) NOT NULL,
  `activity` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'FOLLOW or REGISTRATION',
  `state` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_by` int(11) NOT NULL COMMENT 'Org-entity that performed the operation',
  `added_on` datetime NOT NULL,
  `reference_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Request id of api call',
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_on` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lookupEntity` (`org_id`,`entity_type`,`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : entity_life_cycle
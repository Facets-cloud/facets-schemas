
-- start  Schema : store_tills

CREATE TABLE `store_tills` (
  `entity_id` int(11) NOT NULL COMMENT 'it can be store id or till id',
  `org_id` int(11) NOT NULL,
  `type` enum('STORE','TILL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(3) DEFAULT '1',
  `till_id` int(11) DEFAULT NULL COMMENT 'if type is store, it will pick the first till of it',
  `till_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `till_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `till_username` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_id` int(11) NOT NULL,
  `store_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_external_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_external_id_1` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_external_id_2` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zone_id` int(11) DEFAULT NULL,
  `zone_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zone_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `concept_id` int(11) DEFAULT NULL,
  `concept_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `concept_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`entity_id`),
  KEY `type` (`org_id`,`type`,`entity_id`,`is_active`),
  KEY `entity_id` (`org_id`,`entity_id`,`type`,`is_active`),
  KEY `till_id` (`org_id`,`till_id`),
  KEY `till_code` (`org_id`,`till_code`),
  KEY `till_username` (`org_id`,`till_username`),
  KEY `store_id` (`org_id`,`store_id`),
  KEY `store_code` (`org_id`,`store_code`),
  KEY `store_external_id` (`org_id`,`store_external_id`),
  KEY `zone_id` (`org_id`,`zone_id`),
  KEY `zone_code` (`org_id`,`zone_code`),
  KEY `concept_id` (`org_id`,`concept_id`),
  KEY `concept_code` (`org_id`,`concept_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_tills
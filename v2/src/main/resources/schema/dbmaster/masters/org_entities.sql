
-- start  Schema : org_entities

CREATE TABLE `org_entities` (
  `id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `type` enum('ZONE','CONCEPT','STORE','TILL','STR_SERVER','ADMIN_USER','ASSOCIATE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `is_ou_enabled` tinyint(1) DEFAULT NULL,
  `admin_type` enum('ADMIN','GENERAL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'GENERAL',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'can be username, zone code etc',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Zone nice name , store name, terminal name',
  `description` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `time_zone_id` int(11) DEFAULT NULL,
  `currency_id` int(11) DEFAULT NULL,
  `language_id` int(11) DEFAULT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_code_type` (`org_id`,`code`,`type`),
  KEY `org_id` (`org_id`),
  KEY `time_zone_id` (`time_zone_id`),
  KEY `currency_id` (`currency_id`),
  KEY `language_id` (`language_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_entities
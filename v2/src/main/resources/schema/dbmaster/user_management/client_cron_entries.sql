
-- start  Schema : client_cron_entries

CREATE TABLE `client_cron_entries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron_pattern` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron_type` enum('SYNC','EXECUTE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron_params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled_at_stores_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Contains entries about the client cron ';


-- end  Schema : client_cron_entries
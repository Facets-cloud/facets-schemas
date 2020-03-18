
-- start  Schema : event_manager_effects_log

CREATE TABLE `event_manager_effects_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL COMMENT 'organization under which the instruction set was to be executed',
  `unique_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` int(11) NOT NULL COMMENT 'customer for whom the instructions were to be executed',
  `event_type_id` int(11) NOT NULL COMMENT 'event under which the instruction was created',
  `dump_format` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Whether the exported format is xml / json etc',
  `effects_dump` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('COMPILED','COMMITTED') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'status of the instruction set',
  `executed_on` datetime NOT NULL,
  `compile_request_id` int(11) NOT NULL DEFAULT '-1' COMMENT 'Request ID so that it can be extracted later on',
  `compile_start_millis` bigint(11) NOT NULL COMMENT 'when the compile started',
  `compile_time_millis` int(11) NOT NULL COMMENT 'How long it look to finish compile',
  `commit_request_id` int(11) NOT NULL DEFAULT '-1' COMMENT 'Request ID so that it can be extracted',
  `commit_start_millis` bigint(11) NOT NULL COMMENT 'when the commit started',
  `commit_time_millis` int(11) NOT NULL COMMENT 'how long it took to finish committing',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`unique_id`) KEY_BLOCK_SIZE=8
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='stores the log of the instructions being created';


-- end  Schema : event_manager_effects_log
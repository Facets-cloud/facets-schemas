
-- start  Schema : endpoint_effects_log

CREATE TABLE `endpoint_effects_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL COMMENT 'organization under which the instruction set was to be executed',
  `unique_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` int(11) NOT NULL COMMENT 'customer for whom the instructions were to be executed',
  `event_type_id` int(11) NOT NULL COMMENT 'event under which the instruction was created',
  `effects_dump` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('COMPILED','COMMITTED') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'status of the instruction set',
  `executed_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`unique_id`) KEY_BLOCK_SIZE=8,
  KEY `auto_update_time_index` (`auto_update_time`) KEY_BLOCK_SIZE=8
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='stores the log of the effects being created';


-- end  Schema : endpoint_effects_log

-- start  Schema : event_endpoint_replay

CREATE TABLE `event_endpoint_replay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `endpoint_id` tinyint(4) NOT NULL COMMENT 'id of the endpoint',
  `event_replay_id` bigint(20) NOT NULL COMMENT 'id of the event_replay',
  `status` enum('SUCCESS','FAIL','PERMANENT_FAIL','EVALUATE_FAIL','EVALUATE_PERMANENT_FAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `replay_time` datetime NOT NULL COMMENT 'time at which the event is fired for the endpoint',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `external_responses` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `event` (`endpoint_id`,`org_id`,`event_replay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : event_endpoint_replay
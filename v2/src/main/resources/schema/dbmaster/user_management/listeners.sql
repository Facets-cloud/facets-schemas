
-- start  Schema : listeners

CREATE TABLE `listeners` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `event_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_reference_id` bigint(20) DEFAULT NULL COMMENT 'ID of the event instance thats calling this listener',
  `listener_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `listener_condition` longtext COLLATE utf8mb4_unicode_ci,
  `listener_params` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `execution_order` int(11) NOT NULL DEFAULT '0',
  `created_by` bigint(20) NOT NULL,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `exec_in_zone` longtext COLLATE utf8mb4_unicode_ci,
  `exec_in_stores_json` longtext COLLATE utf8mb4_unicode_ci,
  `event_type` enum('LISTENER','REMINDER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'LISTENER',
  `start_time` datetime DEFAULT NULL COMMENT 'listener will be active after this time',
  `end_time` datetime DEFAULT NULL COMMENT 'listener will be disabled after this time',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : listeners
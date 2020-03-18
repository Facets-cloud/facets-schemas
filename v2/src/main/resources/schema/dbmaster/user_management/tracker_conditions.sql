
-- start  Schema : tracker_conditions

CREATE TABLE `tracker_conditions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the condition for the tracker',
  `org_id` bigint(20) NOT NULL,
  `tracker_id` int(11) NOT NULL,
  `params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'JSON encoded parameters of the condition',
  `listener_id` int(11) DEFAULT NULL,
  `rank` int(11) NOT NULL,
  `milestone_found_template` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `event_manager_ruleset` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'The ruleset which handles this condition',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`tracker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracker_conditions
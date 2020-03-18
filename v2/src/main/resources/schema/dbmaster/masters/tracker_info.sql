
-- start  Schema : tracker_info

CREATE TABLE `tracker_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `entity` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'name of the column being tracked',
  `tracker_name` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'name of the tracker',
  `max_success_signal` int(11) NOT NULL COMMENT 'Maximum number of times this tracker can signal on success',
  `no_of_success_signalled` int(11) NOT NULL COMMENT 'Number of times this tracker has signalled success',
  `params_json` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'any params needed to extract values',
  `custom_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `expires_on` date DEFAULT NULL,
  `send_milestone_info` tinyint(1) NOT NULL DEFAULT '0',
  `milestone_not_found_template` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Stores information about the tracker';


-- end  Schema : tracker_info
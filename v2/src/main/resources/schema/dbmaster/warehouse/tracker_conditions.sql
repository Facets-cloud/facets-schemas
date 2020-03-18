
-- start  Schema : tracker_conditions

CREATE TABLE `tracker_conditions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `strategy_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `program_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `max_number_success_signals` int(11) NOT NULL,
  `tracking_period` int(11) NOT NULL,
  `expression` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ruleset_id` int(11) NOT NULL,
  `point_category_id` int(11) NOT NULL,
  `last_modified_by` int(11) NOT NULL,
  `last_modified_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tracking_period_type` enum('DAYS','MONTHS') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DAYS',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`strategy_id`),
  KEY `program_id` (`program_id`,`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracker_conditions
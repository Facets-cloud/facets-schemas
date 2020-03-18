
-- start  Schema : expiry_strategies_change_log

CREATE TABLE `expiry_strategies_change_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `program_id` int(11) NOT NULL,
  `point_category_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `strategy_id` int(11) NOT NULL,
  `strategy_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `queued_time` datetime NOT NULL,
  `queued_by` int(11) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `time_taken` float NOT NULL,
  `status` enum('IN_PROGRESS','QUEUED','SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `result` mediumtext COLLATE utf8mb4_unicode_ci,
  `error_message` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : expiry_strategies_change_log
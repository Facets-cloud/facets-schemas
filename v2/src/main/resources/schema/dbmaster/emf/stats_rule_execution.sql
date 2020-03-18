
-- start  Schema : stats_rule_execution

CREATE TABLE `stats_rule_execution` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `owner` enum('DVS_ENDPOINT','POINTSENGINE_ENDPOINT','SOCIAL_ENDPOINT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` int(11) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `ruleset_id` int(11) NOT NULL,
  `rule_id` int(11) NOT NULL,
  `rule_case_action_id` int(11) DEFAULT NULL,
  `rule_case` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `executed_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`ruleset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : stats_rule_execution
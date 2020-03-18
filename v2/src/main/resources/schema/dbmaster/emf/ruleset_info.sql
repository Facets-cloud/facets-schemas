
-- start  Schema : ruleset_info

CREATE TABLE `ruleset_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto-generated id',
  `org_id` int(11) NOT NULL,
  `context_id` bigint(20) NOT NULL DEFAULT '-1',
  `context_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner` enum('DVS_ENDPOINT','POINTSENGINE_ENDPOINT','SOCIAL_ENDPOINT','REFERRAL_ENDPOINT','TIMELINE_ENDPOINT','TARGETLOYALTY_ENDPOINT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `package` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `evaluate` tinyint(1) NOT NULL COMMENT 'Evaluation Type',
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `is_active` tinyint(1) NOT NULL COMMENT 'Whether ruleset is active or not',
  `rule_scope` enum('SERVER','CLIENT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SERVER',
  `created_by` int(11) NOT NULL DEFAULT '-1',
  `created_on` datetime NOT NULL,
  `last_modified` datetime NOT NULL,
  `last_modified_by` int(11) NOT NULL COMMENT 'User ID of user who modified this ruleset last.',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : ruleset_info
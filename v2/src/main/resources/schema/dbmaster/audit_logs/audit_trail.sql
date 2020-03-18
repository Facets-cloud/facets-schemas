
-- start  Schema : audit_trail

CREATE TABLE `audit_trail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL COMMENT 'org_id',
  `user_id` int(11) NOT NULL COMMENT 'maps to loggable users',
  `updated_on` datetime NOT NULL,
  `tracked_class` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'the audit class eg ConfigKey, OrgProfile, Listener, Tracker',
  `tracked_item` int(11) DEFAULT NULL COMMENT 'The item id in the individual table',
  `details` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Captures the audit_trails in the system';


-- end  Schema : audit_trail
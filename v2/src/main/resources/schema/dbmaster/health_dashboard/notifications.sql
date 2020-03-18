
-- start  Schema : notifications

CREATE TABLE `notifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `entity_id` bigint(20) NOT NULL DEFAULT '0',
  `message` mediumtext COLLATE utf8mb4_unicode_ci,
  `cause` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `notification_level` enum('HIGH','LOW') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'LOW',
  `ref_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT 'reference id from audit logs',
  `added_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_added_on_idx` (`org_id`,`added_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : notifications

-- start  Schema : points_categories

CREATE TABLE `points_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'auto generated point category id',
  `org_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `program_id` int(11) NOT NULL COMMENT 'program under which the point category belongs to',
  `is_redeemable` tinyint(1) NOT NULL COMMENT 'Says whether points under this can be redeemed. Only one per program',
  `is_system_generated` tinyint(1) NOT NULL COMMENT 'Whether it is an internal point category for some management',
  `created_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`org_id`,`program_id`,`name`) USING BTREE,
  KEY `auto_time_idx` (`created_on`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_categories

-- start  Schema : mlm_users

CREATE TABLE `mlm_users` (
  `org_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `parent_id` bigint(11) DEFAULT NULL,
  `mlm_code` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `path_to_node` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `subtree_size` int(11) NOT NULL,
  `num_referred` int(11) NOT NULL DEFAULT '0',
  `joined` datetime NOT NULL,
  `added_by` bigint(20) NOT NULL,
  PRIMARY KEY (`org_id`,`user_id`),
  UNIQUE KEY `org_id` (`org_id`,`mlm_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mlm_users
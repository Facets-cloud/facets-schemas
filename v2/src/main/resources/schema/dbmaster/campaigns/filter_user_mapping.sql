
-- start  Schema : filter_user_mapping

CREATE TABLE `filter_user_mapping` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(20) NOT NULL,
  `filter_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_favorite` tinyint(1) NOT NULL DEFAULT '1',
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`user_id`,`filter_type`),
  KEY `org_id1` (`org_id`,`user_id`,`is_favorite`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : filter_user_mapping
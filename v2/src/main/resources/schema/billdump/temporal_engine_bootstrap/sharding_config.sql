
-- start  Schema : sharding_config

CREATE TABLE `sharding_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `org_config_id` int(11) NOT NULL,
  `org_config_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `campaign_id` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','PAUSED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `database_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shard_id` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`org_config_id`),
  UNIQUE KEY `org_id_2` (`org_id`,`org_config_name`),
  KEY `status` (`status`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=791 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : sharding_config
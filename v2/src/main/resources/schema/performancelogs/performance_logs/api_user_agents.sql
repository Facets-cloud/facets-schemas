
-- start  Schema : api_user_agents

CREATE TABLE `api_user_agents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_agent` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique` (`user_agent`)
) ENGINE=InnoDB AUTO_INCREMENT=101366 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : api_user_agents
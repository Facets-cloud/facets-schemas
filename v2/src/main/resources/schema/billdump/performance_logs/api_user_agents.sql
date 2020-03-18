
-- start  Schema : api_user_agents

CREATE TABLE `api_user_agents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_agent` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=588 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : api_user_agents
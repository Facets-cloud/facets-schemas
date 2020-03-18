
-- start  Schema : rules

CREATE TABLE `rules` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action_id` bigint(20) DEFAULT NULL,
  `rule` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : rules
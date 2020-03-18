
-- start  Schema : merge_request_handlers

CREATE TABLE `merge_request_handlers` (
  `id` tinyint(3) NOT NULL AUTO_INCREMENT,
  `priority` tinyint(3) NOT NULL,
  `handler` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('DB','SERVICE') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `handler` (`handler`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : merge_request_handlers

-- start  Schema : supported_tags

CREATE TABLE `supported_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('OPTIN','OPTOUT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPTOUT',
  `channel_id` int(11) NOT NULL,
  `tag` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : supported_tags
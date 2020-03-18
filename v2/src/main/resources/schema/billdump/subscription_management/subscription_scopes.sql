
-- start  Schema : subscription_scopes

CREATE TABLE `subscription_scopes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scope` enum('USER','SYSTEM','GOVT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER',
  `sub_scope` enum('NONE','NDNC','BOUNCED','PDPA') COLLATE utf8mb4_unicode_ci DEFAULT 'NONE',
  `priority` int(11) NOT NULL DEFAULT '-1',
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : subscription_scopes
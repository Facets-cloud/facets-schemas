
-- start  Schema : password_history_zbk

CREATE TABLE `password_history_zbk` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `type` enum('TILL','ADMIN_USER','STR_SERVER','ASSOCIATE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_id` int(11) NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  UNIQUE KEY `id` (`id`),
  KEY `org_id` (`org_id`),
  KEY `org_id_2` (`org_id`,`type`,`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : password_history_zbk
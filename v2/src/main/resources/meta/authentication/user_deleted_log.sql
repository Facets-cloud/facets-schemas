
-- start  Schema : user_deleted_log

CREATE TABLE `user_deleted_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `cluster_region` enum('APAC','APAC-MORE','US','EU') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'APAC',
  `identifier_type` enum('USERNAME','MOBILE','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USERNAME',
  `identifier` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `deleted_by` int(11) NOT NULL,
  `deleted_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ref_id` (`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4657 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_deleted_log
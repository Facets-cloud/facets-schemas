
-- start  Schema : user_attributes

CREATE TABLE `user_attributes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `cluster_region` enum('APAC','APAC-MORE','US','EU') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'APAC',
  `identifier_type` enum('USERNAME','MOBILE','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USERNAME',
  `identifier` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '-1',
  `is_verfiied` tinyint(4) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ref_id_2` (`ref_id`,`identifier_type`),
  UNIQUE KEY `identifier` (`identifier`,`identifier_type`,`cluster_region`),
  UNIQUE KEY `cluster_region` (`cluster_region`,`identifier`),
  KEY `ref_id` (`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=357770 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Stores different attributes for each user.';


-- end  Schema : user_attributes
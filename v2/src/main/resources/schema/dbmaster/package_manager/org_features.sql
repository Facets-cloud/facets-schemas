
-- start  Schema : org_features

CREATE TABLE `org_features` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `feature_id` int(11) NOT NULL,
  `value` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`feature_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4074 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_features
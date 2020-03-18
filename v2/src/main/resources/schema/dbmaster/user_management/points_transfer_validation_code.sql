
-- start  Schema : points_transfer_validation_code

CREATE TABLE `points_transfer_validation_code` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `from_user_id` int(11) NOT NULL,
  `to_user_id` int(11) NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `valid_upto` datetime NOT NULL,
  `reference` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'coupon code, points, etc for cross check reference if needed',
  `scope` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `added_by` int(11) NOT NULL COMMENT 'till id',
  `added_on` datetime NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `code_user_uni_idx` (`code`,`from_user_id`,`to_user_id`),
  KEY `validity_user_idx` (`from_user_id`,`to_user_id`,`scope`,`valid_upto`,`is_valid`),
  KEY `user_org_idx` (`org_id`,`from_user_id`,`to_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_transfer_validation_code
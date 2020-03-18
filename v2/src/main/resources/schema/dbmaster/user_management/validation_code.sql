
-- start  Schema : validation_code

CREATE TABLE `validation_code` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `valid_upto` datetime NOT NULL,
  `reference` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'coupon code, points, etc for cross check reference if needed',
  `scope` enum('COUPON','POINTS','GIFT_CARD') COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `added_by` int(11) NOT NULL COMMENT 'till id',
  `added_on` datetime NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `code_user_uni_idx` (`code`,`user_id`),
  KEY `validity_user_idx` (`user_id`,`scope`,`valid_upto`,`is_valid`),
  KEY `user_org_idx` (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : validation_code
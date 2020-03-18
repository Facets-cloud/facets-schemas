
-- start  Schema : redeem_points_offline_requests

CREATE TABLE `redeem_points_offline_requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `bill_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `points_to_be_redeemed` int(11) NOT NULL,
  `requested_by` bigint(20) NOT NULL,
  `requested_on` datetime NOT NULL,
  `done` int(1) NOT NULL DEFAULT '0',
  `modified_on` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `error_msg` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : redeem_points_offline_requests
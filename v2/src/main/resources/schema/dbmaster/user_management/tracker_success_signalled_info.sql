
-- start  Schema : tracker_success_signalled_info

CREATE TABLE `tracker_success_signalled_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `tracker_id` int(11) NOT NULL,
  `tracker_condition_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `signalled_on` datetime NOT NULL,
  `assoc_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`tracker_id`,`user_id`,`tracker_condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracker_success_signalled_info

-- start  Schema : loyalty_tracker

CREATE TABLE `loyalty_tracker` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `num_bills` int(11) DEFAULT NULL,
  `tracker_date` date NOT NULL,
  `sales` float DEFAULT NULL,
  `footfall_count` int(11) DEFAULT NULL,
  `captured_regular_bills` int(11) DEFAULT NULL,
  `captured_not_interested_bills` int(11) DEFAULT NULL,
  `captured_enter_later_bills` int(11) DEFAULT NULL,
  `captured_pending_enter_later_bills` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`store_id`,`tracker_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty_tracker
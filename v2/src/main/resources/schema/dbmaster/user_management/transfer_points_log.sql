
-- start  Schema : transfer_points_log

CREATE TABLE `transfer_points_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT 'User who is placing a transfer request',
  `recepient` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Mobile number of the user to whom points are transfered',
  `points` int(11) NOT NULL,
  `added_on` datetime NOT NULL,
  `added_by` int(11) NOT NULL,
  `processed_by` int(11) DEFAULT NULL,
  `processed_on` datetime NOT NULL,
  `status` enum('PROCESSING','CLOSED','REJECTED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PROCESSING',
  `notes` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : transfer_points_log
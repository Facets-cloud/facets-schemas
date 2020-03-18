
-- start  Schema : tracker_blackout_periods

CREATE TABLE `tracker_blackout_periods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `tracker_id` int(11) NOT NULL,
  `period_start` date NOT NULL COMMENT 'start of the period from when the data will be not considered',
  `period_end` date NOT NULL COMMENT 'end of the period till when the data will be not considered',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`tracker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracker_blackout_periods

-- start  Schema : org_timezones

CREATE TABLE `org_timezones` (
  `org_id` int(11) NOT NULL,
  `time_zone_id` int(11) NOT NULL,
  UNIQUE KEY `org_id_2` (`org_id`,`time_zone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_timezones
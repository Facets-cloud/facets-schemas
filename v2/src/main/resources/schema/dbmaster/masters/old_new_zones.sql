
-- start  Schema : old_new_zones

CREATE TABLE `old_new_zones` (
  `old_id` int(11) NOT NULL,
  `new_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  UNIQUE KEY `old_id` (`old_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : old_new_zones
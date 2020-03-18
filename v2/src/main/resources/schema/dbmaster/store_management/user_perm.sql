
-- start  Schema : user_perm

CREATE TABLE `user_perm` (
  `COL 1` varchar(22) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `COL 2` varchar(19) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_perm
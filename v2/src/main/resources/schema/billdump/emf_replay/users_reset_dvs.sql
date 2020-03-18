
-- start  Schema : users_reset_dvs

CREATE TABLE `users_reset_dvs` (
  `org_id` int(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `cnt` bigint(21) NOT NULL DEFAULT '0',
  `total_replay_count` decimal(25,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : users_reset_dvs

-- start  Schema : access_logs

CREATE TABLE `access_logs` (
  `action_id` int(11) NOT NULL,
  `params` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `time` datetime DEFAULT NULL,
  `apache_thread_id` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : access_logs
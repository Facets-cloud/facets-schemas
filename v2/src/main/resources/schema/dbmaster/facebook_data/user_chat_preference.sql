
-- start  Schema : user_chat_preference

CREATE TABLE `user_chat_preference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `page_id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `chat_thread_id` int(11) NOT NULL,
  `chat_preference` enum('SEARCH','FEEDBACK') COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`org_id`,`page_id`,`chat_thread_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : user_chat_preference
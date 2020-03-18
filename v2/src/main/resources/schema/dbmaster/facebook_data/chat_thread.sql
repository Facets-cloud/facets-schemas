
-- start  Schema : chat_thread

CREATE TABLE `chat_thread` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `page_id` varchar(40) NOT NULL,
  `user_id` varchar(40) NOT NULL,
  `created_time` datetime NOT NULL,
  `last_updated_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_id` (`org_id`,`user_id`,`page_id`),
  KEY `last_updated_time` (`page_id`,`user_id`,`last_updated_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : chat_thread
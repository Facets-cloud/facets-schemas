
-- start  Schema : chats

CREATE TABLE `chats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `thread_id` int(11) NOT NULL,
  `user_id` varchar(40) NOT NULL,
  `page_id` varchar(40) NOT NULL,
  `message_id` varchar(40) DEFAULT NULL,
  `content` text,
  `chat_status` enum('SENT','DELIVERED','READ','RECEIVED','FAILED') NOT NULL,
  `action_type` varchar(40) DEFAULT NULL,
  `message_type` varchar(40) DEFAULT NULL,
  `response_of` int(11) DEFAULT '-1',
  `sent_time` datetime DEFAULT NULL,
  `delivered_time` datetime DEFAULT NULL,
  `read_time` datetime DEFAULT NULL,
  `received_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`,`page_id`),
  KEY `org_id` (`org_id`,`thread_id`),
  KEY `chat_status` (`chat_status`,`thread_id`,`sent_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : chats
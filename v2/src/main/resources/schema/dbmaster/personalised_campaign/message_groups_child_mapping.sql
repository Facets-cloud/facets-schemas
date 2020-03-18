
-- start  Schema : message_groups_child_mapping

CREATE TABLE `message_groups_child_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `req_id` int(11) NOT NULL,
  `child_group_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `scheduled_on` datetime NOT NULL,
  `type` enum('NORMAL','EXPERIMENT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('OPEN','SENT') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPEN',
  `message_queue_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `req_id` (`req_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : message_groups_child_mapping
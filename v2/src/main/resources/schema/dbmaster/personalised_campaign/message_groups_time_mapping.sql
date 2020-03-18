
-- start  Schema : message_groups_time_mapping

CREATE TABLE `message_groups_time_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_group_id` int(11) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : message_groups_time_mapping

-- start  Schema : source_comm_channel_type

CREATE TABLE `source_comm_channel_type` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `source_type_id` int(2) NOT NULL,
  `comm_channel_type_id` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `source_type_id` (`source_type_id`,`comm_channel_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : source_comm_channel_type
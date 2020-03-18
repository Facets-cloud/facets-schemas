
-- start  Schema : slave_trackers

CREATE TABLE `slave_trackers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  `client_id` int(11) DEFAULT '-1',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `ref_id` (`ref_id`,`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : slave_trackers
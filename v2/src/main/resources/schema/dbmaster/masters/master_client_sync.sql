
-- start  Schema : master_client_sync

CREATE TABLE `master_client_sync` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `sync` tinyint(4) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `store_task_last_updated` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : master_client_sync
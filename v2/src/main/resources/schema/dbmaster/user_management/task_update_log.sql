
-- start  Schema : task_update_log

CREATE TABLE `task_update_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `task_entry_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `updated_by` int(11) NOT NULL,
  `updated_status` int(11) NOT NULL,
  `updated_time` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`task_id`,`task_entry_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : task_update_log
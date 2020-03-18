
-- start  Schema : task_status

CREATE TABLE `task_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `store_id` int(11) NOT NULL,
  `executer_id` int(11) NOT NULL,
  `updated_by_till_id` int(11) NOT NULL,
  `updated_on` datetime NOT NULL,
  `status` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`task_id`,`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : task_status
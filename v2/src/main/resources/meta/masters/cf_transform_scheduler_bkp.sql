
-- start  Schema : cf_transform_scheduler_bkp

CREATE TABLE `cf_transform_scheduler_bkp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cf_Id` int(11) NOT NULL,
  `status` enum('COMPLETE','IN_PROGRESS','FAILED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `total_count` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `processed_count` int(11) NOT NULL,
  `latest_cfv_id_processed` bigint(20) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cf_Id` (`cf_Id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=366 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : cf_transform_scheduler_bkp
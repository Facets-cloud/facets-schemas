
-- start  Schema : customer_tracked_kpi

CREATE TABLE `customer_tracked_kpi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `program_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `type` enum('TRACKER','CURRENT_POINTS','LIFETIME_POINTS','LIFETIME_PURCHASE','DOWNGRADE_WINDOW_PURCHASE','DOWNGRADE_WINDOW_VISIT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ref_id` int(11) DEFAULT '-1',
  `ref_sub_id` int(11) DEFAULT '-1',
  `name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `value` decimal(15,3) DEFAULT NULL,
  `valid_till` datetime DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`),
  KEY `auto_update_time` (`auto_update_time`),
  KEY `org_customer_idx` (`org_id`,`program_id`,`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : customer_tracked_kpi
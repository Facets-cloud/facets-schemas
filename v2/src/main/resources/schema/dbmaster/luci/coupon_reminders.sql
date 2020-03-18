
-- start  Schema : coupon_reminders

CREATE TABLE `coupon_reminders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `num_days_before_expiry` int(11) NOT NULL,
  `hour_of_day` int(11) NOT NULL,
  `minute_of_hour` int(11) NOT NULL,
  `cron_task_id` int(11) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `next_scheduled_on` datetime NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `coupon_series_id` (`org_id`,`coupon_series_id`),
  KEY `auto_update_time` (`org_id`,`auto_update_time`),
  KEY `next_scheduled_on` (`org_id`,`next_scheduled_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_reminders
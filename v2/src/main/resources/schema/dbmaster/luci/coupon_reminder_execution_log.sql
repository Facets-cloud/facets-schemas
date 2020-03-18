
-- start  Schema : coupon_reminder_execution_log

CREATE TABLE `coupon_reminder_execution_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `coupon_reminder_id` int(11) NOT NULL,
  `status` enum('QUEUED','RUNNING','COMPLETED','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `execution_started_on` datetime NOT NULL,
  `execution_finished_on` datetime DEFAULT NULL,
  `notes` mediumtext COLLATE utf8mb4_unicode_ci,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `coupon_reminder_id` (`org_id`,`coupon_series_id`,`coupon_reminder_id`),
  KEY `status` (`org_id`,`status`),
  KEY `auto_update_time` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_reminder_execution_log
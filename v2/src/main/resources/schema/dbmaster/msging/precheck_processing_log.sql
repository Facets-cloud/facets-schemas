
-- start  Schema : precheck_processing_log

CREATE TABLE `precheck_processing_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `status` enum('USER_REFRESH','POSTPONED','PRECHECK','CLOSED','ERROR') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : precheck_processing_log
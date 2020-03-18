
-- start  Schema : admin_users_verification_log

CREATE TABLE `admin_users_verification_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `admin_user_queue_id` int(11) NOT NULL,
  `status_id` int(2) NOT NULL,
  `job_id` int(11) NOT NULL,
  `whitelisted_status` enum('NOT_PROCESSED','VALID','INVALID','UNKNOWN') DEFAULT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `updated_by` int(11) NOT NULL,
  `entered_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`,`admin_user_queue_id`,`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- end  Schema : admin_users_verification_log
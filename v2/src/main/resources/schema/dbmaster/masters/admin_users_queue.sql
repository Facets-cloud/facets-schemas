
-- start  Schema : admin_users_queue

CREATE TABLE `admin_users_queue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `reports_to` int(11) NOT NULL,
  `title` enum('Mr.','Mrs.','Miss.') DEFAULT NULL,
  `first_name` varchar(250) DEFAULT NULL,
  `middle_name` varchar(250) DEFAULT NULL,
  `last_name` varchar(250) DEFAULT NULL,
  `mobile` varchar(15) NOT NULL,
  `email` varchar(50) NOT NULL,
  `entities_managed` text,
  `created_by` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  `status_id` int(2) NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_type` enum('BRAND_USER','BRAND_USERS_REPORT_ONLY','BRAND_USER_POC') NOT NULL DEFAULT 'BRAND_USER',
  `ref_id` int(11) NOT NULL DEFAULT '-1',
  `locale_id` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`mobile`),
  KEY `org_id_2` (`org_id`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- end  Schema : admin_users_queue

-- start  Schema : etl_run_version

CREATE TABLE `etl_run_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version_id` int(11) NOT NULL,
  `run_id` int(11) NOT NULL,
  `run_date` datetime NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org` (`org_id`,`run_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : etl_run_version
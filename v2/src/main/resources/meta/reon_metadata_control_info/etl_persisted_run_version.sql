
-- start  Schema : etl_persisted_run_version

CREATE TABLE `etl_persisted_run_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `run_id` int(11) NOT NULL,
  `version_id` int(11) NOT NULL DEFAULT '1',
  `run_date` datetime NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_run` (`org_id`,`run_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : etl_persisted_run_version

-- start  Schema : segment_cron

CREATE TABLE `segment_cron` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `segment_id` bigint(20) NOT NULL,
  `pattern` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_run_date` datetime DEFAULT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `segment` (`org_id`,`segment_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_cron
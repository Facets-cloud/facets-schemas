
-- start  Schema : cron_migration_details

CREATE TABLE `cron_migration_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `v1_campaign_cron_id` int(11) DEFAULT NULL,
  `v1_health_cron_id` int(11) DEFAULT NULL,
  `v2_cron_id` int(11) DEFAULT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21291 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : cron_migration_details
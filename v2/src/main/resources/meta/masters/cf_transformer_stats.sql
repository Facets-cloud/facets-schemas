
-- start  Schema : cf_transformer_stats

CREATE TABLE `cf_transformer_stats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `cf_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `count_picked` bigint(20) NOT NULL DEFAULT '0',
  `count_transformed` bigint(20) NOT NULL DEFAULT '0',
  `count_saved` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `unique_date_org_cf` (`date`,`org_id`,`cf_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : cf_transformer_stats

-- start  Schema : cf_transformer_error_logs

CREATE TABLE `cf_transformer_error_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `cf_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assoc_id` bigint(20) NOT NULL,
  `error_log` mediumtext COLLATE utf8mb4_unicode_ci,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `idx_org_cf` (`auto_update_time`,`org_id`,`cf_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1635403 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : cf_transformer_error_logs

-- start  Schema : customer_base_store_log

CREATE TABLE `customer_base_store_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `base_store` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT 'instore',
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : customer_base_store_log
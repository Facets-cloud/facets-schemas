
-- start  Schema : user_preference

CREATE TABLE `user_preference` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto-generated id',
  `org_id` bigint(20) NOT NULL,
  `page_id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `preference` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `default_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_by` int(11) NOT NULL DEFAULT '-1',
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`,`page_id`,`preference`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : user_preference
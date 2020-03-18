
-- start  Schema : user_preference_values

CREATE TABLE `user_preference_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto-generated id',
  `org_id` bigint(20) NOT NULL,
  `page_id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fb_id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_preference_id` int(11) NOT NULL,
  `value` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`,`page_id`,`fb_id`,`user_preference_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : user_preference_values
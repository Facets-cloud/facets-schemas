
-- start  Schema : task

CREATE TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `body` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `expiry_date` date NOT NULL,
  `valid_days_from_create` int(11) NOT NULL,
  `is_memo` tinyint(1) NOT NULL,
  `statuses` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tags` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  `updated_by` int(11) NOT NULL,
  `updated_on` datetime NOT NULL,
  `action_type` enum('sms','email','call','none') COLLATE utf8mb4_unicode_ci NOT NULL,
  `action_template` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by_type` enum('manager','cashier','admin_user') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by_id` int(11) NOT NULL,
  `executable_by_type` enum('store','cashier') COLLATE utf8mb4_unicode_ci NOT NULL,
  `executable_by_ids` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `execute_by_all` tinyint(1) NOT NULL,
  `customer_target` tinyint(1) NOT NULL,
  `comment` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : task
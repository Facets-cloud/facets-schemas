
-- start  Schema : wish_list

CREATE TABLE `wish_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `org_source_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_date_time` datetime DEFAULT NULL,
  `privacy_level` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Decide if the list is public or private or shared with a group',
  `description` text COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `modified_date_time` datetime DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `uniqueWishlistInOrgSource` (`org_id`,`user_id`,`org_source_id`),
  KEY `user_id` (`user_id`,`org_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : wish_list

-- start  Schema : wish_list_items

CREATE TABLE `wish_list_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `wish_list_id` bigint(20) NOT NULL,
  `product_sku` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'variant product id if available',
  `rank` int(11) DEFAULT NULL,
  `comments` text COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_date_time` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_date_time` datetime NOT NULL,
  `modified_by` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`,`id`),
  UNIQUE KEY `uniqueItemSku` (`wish_list_id`,`product_sku`),
  KEY `wish_list_id` (`wish_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : wish_list_items
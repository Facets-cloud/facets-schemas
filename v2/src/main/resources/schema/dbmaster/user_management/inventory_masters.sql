
-- start  Schema : inventory_masters

CREATE TABLE `inventory_masters` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `item_sku` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `item_ean` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` float NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `img_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `is_valid` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`item_sku`),
  UNIQUE KEY `org_id_2` (`org_id`,`item_ean`),
  KEY `org_time_idx` (`org_id`,`added_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Details about an item';


-- end  Schema : inventory_masters
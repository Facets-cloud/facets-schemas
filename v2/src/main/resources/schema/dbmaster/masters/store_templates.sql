
-- start  Schema : store_templates

CREATE TABLE `store_templates` (
  `store_id` int(11) NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `s_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s_email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s_mobile` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `e_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `e_mobile` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `e_email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `e_land_line` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s_land_line` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s_add` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `e_add` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s_extra` mediumtext COLLATE utf8mb4_unicode_ci,
  `e_extra` mediumtext COLLATE utf8mb4_unicode_ci,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : store_templates
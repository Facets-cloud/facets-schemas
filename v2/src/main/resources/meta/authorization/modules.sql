
-- start  Schema : modules

CREATE TABLE `modules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '''''',
  `is_web_client` tinyint(1) NOT NULL DEFAULT '0',
  `version` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1.0.0.0',
  `namespace` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `display_order` tinyint(4) NOT NULL DEFAULT '0',
  `visible_to_customer` tinyint(4) NOT NULL DEFAULT '1',
  `is_free_module` tinyint(4) NOT NULL DEFAULT '0',
  `notes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : modules
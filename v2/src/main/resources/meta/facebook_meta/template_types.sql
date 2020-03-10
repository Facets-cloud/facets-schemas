
-- start  Schema : template_types

CREATE TABLE `template_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto-generated id',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `channel` enum('FACEBOOK') COLLATE utf8mb4_unicode_ci NOT NULL,
  `default_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` int(11) NOT NULL DEFAULT '-1',
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : template_types
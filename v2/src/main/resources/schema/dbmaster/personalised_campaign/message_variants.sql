
-- start  Schema : message_variants

CREATE TABLE `message_variants` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_group_id` int(11) NOT NULL,
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `default` tinyint(1) NOT NULL,
  `message_type_id` int(11) NOT NULL,
  `ref_id` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `message_group_id` (`message_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : message_variants
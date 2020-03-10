
-- start  Schema : custom_dimension_insights_hierarchy

CREATE TABLE `custom_dimension_insights_hierarchy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) DEFAULT NULL,
  `dimension_id` int(11) DEFAULT NULL,
  `priority` tinyint(4) DEFAULT NULL,
  `base_dimension` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : custom_dimension_insights_hierarchy
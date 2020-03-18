
-- start  Schema : stores_add_ons

CREATE TABLE `stores_add_ons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `vm` tinyint(1) DEFAULT NULL,
  `demographics` tinyint(1) DEFAULT NULL,
  `heatmap` tinyint(1) DEFAULT NULL,
  `speech` tinyint(1) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_id_type` (`org_id`,`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : stores_add_ons
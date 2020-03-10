
-- start  Schema : target_data

CREATE TABLE `target_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_id` int(11) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `store_id` int(11) DEFAULT NULL,
  `target` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `date_org_store_udx` (`org_id`,`store_id`,`date_id`)
) ENGINE=InnoDB AUTO_INCREMENT=358 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : target_data
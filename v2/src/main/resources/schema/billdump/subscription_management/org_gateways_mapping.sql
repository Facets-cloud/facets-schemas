
-- start  Schema : org_gateways_mapping

CREATE TABLE `org_gateways_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `gateway_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_ga` (`org_id`,`gateway_name`),
  KEY `auto_update_time` (`auto_update_time`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_gateways_mapping
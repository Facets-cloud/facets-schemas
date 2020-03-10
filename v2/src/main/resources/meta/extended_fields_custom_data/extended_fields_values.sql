
-- start  Schema : extended_fields_values

CREATE TABLE `extended_fields_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_on` datetime NOT NULL,
  `ef_id` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_value` (`org_id`,`value`,`ef_id`),
  KEY `ef_idOrgIdAndId` (`ef_id`,`org_id`,`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1226550 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : extended_fields_values
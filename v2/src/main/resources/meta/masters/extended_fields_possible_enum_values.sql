
-- start  Schema : extended_fields_possible_enum_values

CREATE TABLE `extended_fields_possible_enum_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ef_id` int(11) NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  `value` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  `modified_by` int(11) NOT NULL,
  `modified_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ef_scope_id` (`ef_id`,`org_id`,`value`)
) ENGINE=InnoDB AUTO_INCREMENT=7239 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : extended_fields_possible_enum_values
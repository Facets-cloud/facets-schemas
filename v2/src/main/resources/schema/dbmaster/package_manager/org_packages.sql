
-- start  Schema : org_packages

CREATE TABLE `org_packages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL,
  `service_type_id` int(11) NOT NULL,
  `type` enum('APP','PACKAGE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_date` date NOT NULL,
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`ref_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1800 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_packages
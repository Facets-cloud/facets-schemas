
-- start  Schema : client_version_mapping

CREATE TABLE `client_version_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `version_id` int(11) NOT NULL,
  `version_set_on` datetime NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `version_set_by` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`,`store_id`),
  KEY `version_idx` (`store_id`,`is_active`)
) ENGINE=InnoDB AUTO_INCREMENT=108589 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Contains the mapping of the versions set for the store';


-- end  Schema : client_version_mapping
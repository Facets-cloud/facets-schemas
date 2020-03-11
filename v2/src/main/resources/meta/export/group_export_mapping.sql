
-- start  Schema : group_export_mapping

CREATE TABLE `group_export_mapping` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(11) NOT NULL,
  `export_mapping_id` bigint(11) NOT NULL,
  `campaign_id` bigint(11) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `peer_group_id` bigint(20) NOT NULL,
  `count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  KEY `export_mapping_id` (`export_mapping_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : group_export_mapping
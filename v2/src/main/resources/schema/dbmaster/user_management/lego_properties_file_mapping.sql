
-- start  Schema : lego_properties_file_mapping

CREATE TABLE `lego_properties_file_mapping` (
  `org_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `file_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`org_id`,`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : lego_properties_file_mapping
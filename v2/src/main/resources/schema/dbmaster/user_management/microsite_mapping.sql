
-- start  Schema : microsite_mapping

CREATE TABLE `microsite_mapping` (
  `org_id` bigint(20) NOT NULL,
  `microsite_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`org_id`),
  UNIQUE KEY `microsite_id` (`microsite_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : microsite_mapping
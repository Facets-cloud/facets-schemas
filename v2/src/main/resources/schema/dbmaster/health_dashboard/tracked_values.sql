
-- start  Schema : tracked_values

CREATE TABLE `tracked_values` (
  `trackable_entity_id` bigint(20) DEFAULT NULL,
  `value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` int(11) NOT NULL,
  `entity_ref_id` bigint(20) NOT NULL,
  `updated_on` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `is_valid` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tracked_values
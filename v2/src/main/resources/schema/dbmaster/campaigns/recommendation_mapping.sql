
-- start  Schema : recommendation_mapping

CREATE TABLE `recommendation_mapping` (
  `org_id` bigint(20) NOT NULL,
  `campaign_id` bigint(20) NOT NULL,
  `recommendation_plan_id` bigint(20) NOT NULL,
  `num_of_recommendations` tinyint(3) DEFAULT NULL,
  `num_of_attributes` tinyint(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : recommendation_mapping
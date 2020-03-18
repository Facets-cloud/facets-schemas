
-- start  Schema : survey_mapping

CREATE TABLE `survey_mapping` (
  `org_id` bigint(20) NOT NULL,
  `campaign_id` bigint(20) NOT NULL,
  `ref_campaign_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : survey_mapping
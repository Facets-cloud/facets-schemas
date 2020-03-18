
-- start  Schema : org_personalise_campaign_mapping

CREATE TABLE `org_personalise_campaign_mapping` (
  `org_id` int(11) NOT NULL,
  `is_personalised_enable` tinyint(1) NOT NULL DEFAULT '0',
  `is_brand_category_enable` tinyint(1) NOT NULL DEFAULT '0',
  `last_modified_by` int(11) NOT NULL,
  `last_modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : org_personalise_campaign_mapping
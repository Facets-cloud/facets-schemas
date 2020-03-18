
-- start  Schema : audience_groups

CREATE TABLE `audience_groups` (
  `id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `audience_provider` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `params` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('LOYALTY','NON_LOYALTY','ALL') CHARACTER SET utf8mb4 NOT NULL DEFAULT 'LOYALTY',
  PRIMARY KEY (`id`,`org_id`),
  KEY `campaign_id` (`campaign_id`,`audience_provider`),
  KEY `type` (`org_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : audience_groups
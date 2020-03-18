
-- start  Schema : campaign_tags

CREATE TABLE `campaign_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `updated_by` int(11) NOT NULL,
  `tags` text COLLATE utf8mb4_unicode_ci,
  `created_date` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : campaign_tags
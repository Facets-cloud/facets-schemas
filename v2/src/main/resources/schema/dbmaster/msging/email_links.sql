
-- start  Schema : email_links

CREATE TABLE `email_links` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `messageId` int(11) NOT NULL,
  `url` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `campaign_id` bigint(20) NOT NULL DEFAULT '-1',
  `group_id` bigint(20) NOT NULL DEFAULT '-1',
  `org_id` bigint(20) NOT NULL DEFAULT '-1',
  `last_read_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `messageId` (`messageId`),
  KEY `campaign_id` (`campaign_id`,`group_id`,`org_id`),
  KEY `messageId_2` (`messageId`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_links
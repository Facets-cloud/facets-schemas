
-- start  Schema : email_links_redirection

CREATE TABLE `email_links_redirection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `message_id` bigint(20) NOT NULL DEFAULT '-1',
  `url` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `clicks` smallint(6) NOT NULL DEFAULT '0',
  `type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CAMPAIGN',
  `campaign_id` int(11) DEFAULT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `inbox_id` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_links_redirection
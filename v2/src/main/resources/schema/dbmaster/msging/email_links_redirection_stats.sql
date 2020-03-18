
-- start  Schema : email_links_redirection_stats

CREATE TABLE `email_links_redirection_stats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL COMMENT 'refers to email redirection link',
  `user_id` int(11) NOT NULL,
  `clicks` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `ref_id` (`ref_id`,`user_id`),
  KEY `org_id` (`org_id`,`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_links_redirection_stats
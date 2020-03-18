
-- start  Schema : email_link_user_stats

CREATE TABLE `email_link_user_stats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL COMMENT 'points to email redirection stats',
  `browser_id` int(11) NOT NULL DEFAULT '-1',
  `mobile_id` int(11) NOT NULL DEFAULT '-1',
  `platform_id` int(11) NOT NULL DEFAULT '-1',
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`user_id`,`ref_id`),
  KEY `last_updated_on` (`last_updated_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_link_user_stats
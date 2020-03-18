
-- start  Schema : nsadmin_skipped_logs

CREATE TABLE `nsadmin_skipped_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `resolved_tags` mediumtext COLLATE utf8mb4_unicode_ci,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : nsadmin_skipped_logs

-- start  Schema : import_subscribed_profile_stages

CREATE TABLE `import_subscribed_profile_stages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `profile_id` int(10) unsigned NOT NULL,
  `stage_type` enum('ALL','TEMP_DB_INSERT','VALIDATIONS','MAINDB_COMPLETE','DAILY_SUMMARY','PENDING_NOTIFY','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_subscribe_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_subscribe_stage` (`group_subscribe_id`,`profile_id`,`stage_type`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_subscribed_profile_stages
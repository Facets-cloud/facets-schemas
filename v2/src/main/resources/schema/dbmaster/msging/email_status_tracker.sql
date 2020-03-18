
-- start  Schema : email_status_tracker

CREATE TABLE `email_status_tracker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_run_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2562 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : email_status_tracker
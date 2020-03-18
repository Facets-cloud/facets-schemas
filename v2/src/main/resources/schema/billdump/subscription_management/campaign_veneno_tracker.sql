
-- start  Schema : campaign_veneno_tracker

CREATE TABLE `campaign_veneno_tracker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_inserted_id` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28687 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : campaign_veneno_tracker
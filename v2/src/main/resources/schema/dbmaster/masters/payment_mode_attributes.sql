
-- start  Schema : payment_mode_attributes

CREATE TABLE `payment_mode_attributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payment_mode_id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `data_type` enum('STRING','INT','FLOAT','BOOL','DATE','TYPED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'STRING',
  `regex` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Applicable when free flowing text field -STRING',
  `default_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `error_msg` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `payment_mode_id` (`payment_mode_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : payment_mode_attributes
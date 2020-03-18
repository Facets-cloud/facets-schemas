
-- start  Schema : payment_mode_attribute_possible_values

CREATE TABLE `payment_mode_attribute_possible_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payment_mode_id` int(11) NOT NULL,
  `payment_mode_attribute_id` int(11) NOT NULL,
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `value` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `added_by` bigint(20) DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `payment_mode_attribute_value_idx` (`payment_mode_attribute_id`,`value`),
  KEY `payment_mode_id_idx` (`payment_mode_id`),
  KEY `time_idx` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : payment_mode_attribute_possible_values
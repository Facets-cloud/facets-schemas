
-- start  Schema : org_payment_mode_attribute_possible_values

CREATE TABLE `org_payment_mode_attribute_possible_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `org_payment_mode_id` int(11) NOT NULL,
  `org_payment_mode_attribute_id` int(11) NOT NULL,
  `payment_mode_attribute_possible_value_id` int(11) DEFAULT NULL COMMENT 'Can optionally be in payment attr value',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `value` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `added_by` bigint(20) DEFAULT '0',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_payment_mode_attribute_value_idx` (`org_id`,`org_payment_mode_attribute_id`,`value`),
  KEY `org_payment_mode_id_idx` (`org_id`,`org_payment_mode_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_payment_mode_attribute_possible_values
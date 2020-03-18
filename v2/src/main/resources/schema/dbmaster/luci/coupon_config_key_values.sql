
-- start  Schema : coupon_config_key_values

CREATE TABLE `coupon_config_key_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL COMMENT 'id of the voucher_series table',
  `value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_by` int(11) NOT NULL COMMENT 'user who updated the key',
  `updated_on` datetime NOT NULL COMMENT 'time when the key is updated',
  `is_valid` tinyint(1) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_update_time` (`org_id`,`auto_update_time`),
  KEY `coupon_series_id` (`org_id`,`coupon_series_id`,`key_id`,`is_valid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_config_key_values
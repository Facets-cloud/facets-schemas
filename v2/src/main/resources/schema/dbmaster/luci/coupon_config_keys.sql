
-- start  Schema : coupon_config_keys

CREATE TABLE `coupon_config_keys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `default_value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'used only in case of initial configuration',
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_config_keys
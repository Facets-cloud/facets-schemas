
-- start  Schema : voucher_product_data_values

CREATE TABLE `voucher_product_data_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `voucher_product_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `voucher_series_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_voucher_product_id_value` (`org_id`,`voucher_series_id`,`voucher_product_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : voucher_product_data_values